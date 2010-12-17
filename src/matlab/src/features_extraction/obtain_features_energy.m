% This function extracts the energy-to-rest ratio feature from
% eeg_structure and stores it in features_out along with all the features
% already present in features_in.
%
% eeg_structure: a structure containing the EEGs.
%   required fields:
%       eeg_structure.
%                  eeg(x).
%                       data = [signals-by-channels matrix]
%                       attack = {true, false}
%                       begin = <int>
%                  sample_frequency = <double>
%   data contains all the signals on all the channels, attack can either be
%   true or false, depending on whether or not an attack has happened in
%   this eeg, and iif attack == true begin is loaded with the instant when 
%   the attack had begun. sample_frequency is the frequency at which the
%   eegs are sampled.
% features_in: a structure with already loaded features (extracted with
% this procedure - or else, an empty structure)
% left_out_seconds, predict_horizon: equation for prediction interval is the
% following: [t+left_out_seconds, t+predict_horizon]
% window_size: time slice considered for feature extraction
function [features_out] = obtain_features_energy(eeg_structure, features_in, left_out_seconds, window_size, predict_horizon)
    % features_copy
    features_out = features_in;

    % Sample rate
    freq = eeg_structure.sample_frequency;

    % "Geometry" of eeg
    num_of_eegs = length(eeg_structure.eeg);
    num_of_channels = size(eeg_structure.eeg(1).data, 2);

    % wavelet parameters
    num_of_decompositions = 8;
    wavelet = 'db4';

    % =================================================
    % Extract mean energy from recordings with seizures
    % =================================================
    for i = 1:num_of_eegs
        % cut EEG of time channel and post attack phase, if any
        EEG = eeg_structure.eeg(i).data(:, 2:end);
        time_included = size(EEG, 1);
        % if an attack happened, recompute time
        if (eeg_structure.eeg(i).attack == true)
            time_included = eeg_structure.eeg(i).begin - left_out_seconds*freq;
            if (time_included <= 0)
                err = MException('You tried to leave out too many seconds.');
                left_out_seconds %#ok<NOPRT>
                eeg_structure.eeg(i).begin
                i %#ok<NOPRT>
                time_included %#ok<NOPRT>
                throw(err);
            end
        end
        % take the useful part of the EEG
        EEG = EEG(1:time_included, :);
        
        % precompute the number of windows (1 per second)
        windows_number = size(EEG,1)/freq - window_size;
        
        % init output
        fprintf('Progress of current EEG: \n');
        previous_print = 0;
        
        % ================================================
        % Compute reference (time & time-frequency domain)
        % ================================================
        % precompute the energy of the signal "far far away" (take first 10
        % seconds)
        % take first (at most) ten seconds
        if (time_included >= 10*freq)
            channels_10_seconds = EEG(1:freq*10, :);
        else
            channels_10_seconds = EEG(1:time_included, :);
        end
        % for each channel
        for channel_number=1:num_of_channels
            % compute ref for each channel
            channel = channels_10_seconds(:, channel_number);
            reference(channel_number).time_domain = mean(abs(channel)); %#ok<AGROW>
            
            % wavelet
            [c, l] = wavedec(channel, num_of_decompositions, wavelet);
            
            % compute ref for decomposition for channel
            for decomp_number = 1:num_of_decompositions
                % separate data
                wavelet_coefficients = wrcoef('d', c, l, wavelet, decomp_number);
                
                % assign mean coefficients
                reference(channel_number).decomposition(decomp_number) = mean(abs(wavelet_coefficients(channel_number, decomp_number, :))); %#ok<AGROW>
            end
        end
        
        % For each window...
        for current_window = 1:windows_number
            % ===========================
            % Print advancements every 5%
            % ===========================
            if (i/windows_number*100-previous_print > 5)
                previous_print = i/windows_number*100;
                fprintf('.');
            end
            
            % ====================
            % Compute token bounds
            % ====================
            token_end   = (current_window - 1 + window_size) * freq;
            token_start = token_end - window_size * freq + 1;
            window      = EEG(token_start:token_end, 1:end);
            
            % ================================
            % Compute the energy-to-rest ratio
            % ================================
            % extract energy-to-rest ratio on channel and wavelet
            for channel_number=1:num_of_channels
                % take channel and compute geometry
                channel = window(:, channel_number);
                
                % compute bounds
                first_stop_thirds  = floor(1*length(token)/3);
                second_stop_thirds = floor(2*length(token)/3);
                first_stop_fifths  = floor(1*length(token)/5);
                second_stop_fifths = floor(2*length(token)/5);
                third_stop_fifths  = floor(3*length(token)/5);
                fourth_stop_fifths = floor(4*length(token)/5);
                
                % store time domain reference
                reference = reference(channel_number).time_domain;
                
                % ====================
                % Time domain analysis
                % ====================
                % whole
                data = mean(abs(channel))/reference;
                features_out.eeg(i).mean_energy(current_window).channel(channel_number).whole  = {data};
                
                % thirds
                elem_1 = mean(abs(channel(1:first_stop_thirds)))/reference;
                elem_2 = mean(abs(channel(first_stop_thirds+1:second_stop_thirds)))/reference;
                elem_3 = mean(abs(channel(second_stop_thirds+1:end)))/reference;
                data = [elem_1, elem_2, elem_3];
                features_out.eeg(i).mean_energy(current_window).channel(channel_number).thirds = {data};
                
                % fifths
                elem_1 = mean(abs(channel(1:first_stop_fifths)))/reference;
                elem_2 = mean(abs(channel(first_stop_fifths+1:second_stop_fifths)))/reference;
                elem_3 = mean(abs(channel(second_stop_fifths+1:third_stop_fifths)))/reference;
                elem_4 = mean(abs(channel(third_stop_fifths+1:fourth_stop_fifths)))/reference;
                elem_5 = mean(abs(channel(fourth_stop_fifths+1:end)))/reference;
                data = [elem_1, elem_2, elem_3, elem_4, elem_5];
                features_out.eeg(i).mean_energy(current_window).channel(channel_number).fifths = {data};
                
                % ==============================
                % Time-frequency domain analysis
                % ==============================
                % wavelet from current channel
                [c, l] = wavedec(channel, num_of_decompositions, wavelet);
                % for each decomposition compute feature
                for decomp_number = 1:num_of_decompositions
                    % separate data
                    channel_decomposition = wrcoef('d', c, l, wavelet, decomp_number);
                    
                    % store time-frequency domain reference
                    channel_decomposition_reference = reference(channel_number).decomposition(decomp_number);
                    
                    % whole
                    data = mean(abs(channel_decomposition))/channel_decomposition_reference;
                    features_out.eeg(i).mean_energy(current_window).channel(channel_number).decomposition(decomp_number).whole  = {data};
                    
                    % thirds
                    elem_1 = mean(abs(channel_decomposition(1:first_stop_thirds)))/reference;
                    elem_2 = mean(abs(channel_decomposition(first_stop_thirds+1:second_stop_thirds)))/reference;
                    elem_3 = mean(abs(channel_decomposition(second_stop_thirds+1:end)))/reference;
                    data = [elem_1, elem_2, elem_3];
                    features_out.eeg(i).mean_energy(current_window).channel(channel_number).decomposition(decomp_number).thirds = {data};
                    
                    % fifths
                    elem_1 = mean(abs(channel_decomposition(1:first_stop_fifths)))/reference;
                    elem_2 = mean(abs(channel_decomposition(first_stop_fifths+1:second_stop_fifths)))/reference;
                    elem_3 = mean(abs(channel_decomposition(second_stop_fifths+1:third_stop_fifths)))/reference;
                    elem_4 = mean(abs(channel_decomposition(third_stop_fifths+1:fourth_stop_fifths)))/reference;
                    elem_5 = mean(abs(channel_decomposition(fourth_stop_fifths+1:end)))/reference;
                    data = [elem_1, elem_2, elem_3, elem_4, elem_5];
                    features_out.eeg(i).mean_energy(current_window).channel(channel_number).decomposition(decomp_number).fifths = {data};
                end
            end

            % determine class
            if ((current_window + window_size + predict_horizon) > eeg_structure.eeg(i).begin)
                label = 1;
            else
                label = 0;
            end
            features_out.eeg(i).mean_energy(current_window).label = label;
        end
        fprintf('\n');
    end
end

