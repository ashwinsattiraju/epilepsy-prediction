function [fraction remaining] = create_subset_from_features_separated_wc_first(features, num_samples_max)
	% compute num of eeg registrations and words per eeg
    eeg_registrations = length(features.eeg);
    eeg_words_per_registration = zeros(eeg_registrations,1);
    for i=1:eeg_registrations
        eeg_words_per_registration(i) = length(features.eeg(i).label);
    end
    
    % compute the number of windows that must be taken from the original
    % set and put into the fraction dataset
    total_words = 0;
    for i=1:eeg_registrations
        total_words = total_words + eeg_words_per_registration(i);
    end
    total_words_in_fraction           = num_samples_max;
    total_words_in_fraction_attack    = floor(total_words_in_fraction/2);
    total_words_in_fraction_no_attack = total_words_in_fraction-total_words_in_fraction_attack;
    
    % compute random order
    s1 = RandStream.create('mrg32k3a','NumStreams',1);
    next_window = randperm(s1, total_words);
    
    % next attack window index - increment whenever a new window is added
    % to attacks
    next_window_remaining_index = ones(eeg_registrations, 1) ;
    next_window_fraction_index  = ones(eeg_registrations, 1) ;
    
    % compute the fraction and the remaining dataset
    % PAY ATTENTION
    % the resulting dataset may not have any word in some eeg registration
    % after the processing both in fraction and in remaining
    for i=1:total_words
        fprintf('Resize of dataset in progress: %.4g %%\n', i/total_words*100);
        % get eeg to which we are referring to (random due to preceding randperm)
        eeg_num = 0;
        found = false;
        % this is the window we are referring to but me must compute where 
        % it is
        discovery_next_eeg = next_window(i);
        while (~found)
            eeg_num = eeg_num + 1;
            if (eeg_num==eeg_registrations)
                found = true; % this window was in the last eeg :) 
            elseif (discovery_next_eeg < eeg_words_per_registration(eeg_num))
                found = true;
            end
            discovery_next_eeg = discovery_next_eeg - eeg_words_per_registration(eeg_num);
        end
        
        % get window inside given eeg to which we are referring to
        window_num = mod(next_window(i), eeg_words_per_registration(eeg_num));
        if (window_num == 0)
            window_num = eeg_words_per_registration(eeg_num); % mod(x,n) -> [0,n-1]
        end
        
        % get word class
        word_class = features.eeg(eeg_num).label(window_num);
        
        % if we need more attacks in subset, take them, normalized
        if (total_words_in_fraction_attack > 0 && word_class==1)
            % copy data structures / 1 
            % [entropy of signal whole/thirds/fifths]
            fraction.entropy_signal_whole(eeg_num). data(next_window_fraction_index(eeg_num)) = features.entropy_signal_whole(eeg_num).data(window_num);
            fraction.entropy_signal_thirds(eeg_num).data(next_window_fraction_index(eeg_num)) = normalize(features.entropy_signal_thirds(eeg_num).data(window_num));
            fraction.entropy_signal_fifths(eeg_num).data(next_window_fraction_index(eeg_num)) = normalize(features.entropy_signal_fifths(eeg_num).data(window_num));
            % copy data structures / 2
            % [kolmogorov of signal whole/thirds/fifths]
            fraction.kolmogorov_signal_whole(eeg_num). data(next_window_fraction_index(eeg_num)) = features.kolmogorov_signal_whole(eeg_num). data(window_num);
            fraction.kolmogorov_signal_thirds(eeg_num).data(next_window_fraction_index(eeg_num)) = normalize(features.kolmogorov_signal_thirds(eeg_num).data(window_num));
            fraction.kolmogorov_signal_fifths(eeg_num).data(next_window_fraction_index(eeg_num)) = normalize(features.kolmogorov_signal_fifths(eeg_num).data(window_num));
            % copy data structure / 3
            % [mean energy of signal whole/thirds/fifths]
            fraction.mean_energy_signal_whole(eeg_num). data(next_window_fraction_index(eeg_num)) = features.mean_energy_signal_whole(eeg_num). data(window_num);
            fraction.mean_energy_signal_thirds(eeg_num).data(next_window_fraction_index(eeg_num)) = normalize(features.mean_energy_signal_thirds(eeg_num).data(window_num));
            fraction.mean_energy_signal_fifths(eeg_num).data(next_window_fraction_index(eeg_num)) = normalize(features.mean_energy_signal_fifths(eeg_num).data(window_num));
            for dec=1:8
                % copy data structures / 4
                % [entropy of wc whole/thirds/fifths, decomposition by decomposition]
                data = normalize(features.entropy_wc_whole(eeg_num). data(window_num)); data = data{1}; data ={data(1+(dec-1):(dec-1)+1)};
                fraction.entropy_wc_whole(eeg_num). decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                data = normalize(features.entropy_wc_thirds(eeg_num).data(window_num)); data = data{1}; data ={data(1+3*(dec-1):3*(dec-1)+3)};
                fraction.entropy_wc_thirds(eeg_num).decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                data = normalize(features.entropy_wc_fifths(eeg_num).data(window_num)); data = data{1}; data = {data(1+5*(dec-1):5*(dec-1)+5)};
                fraction.entropy_wc_fifths(eeg_num).decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                % copy data structures / 5
                % [kolmogorov of wc whole/thirds/fifths, decomposition by decomposition]
                data = normalize(features.kolmogorov_wc_whole(eeg_num). data(window_num)); data = data{1}; data ={data(1+(dec-1):(dec-1)+1)};
                fraction.kolmogorov_wc_whole(eeg_num). decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                data = normalize(features.kolmogorov_wc_thirds(eeg_num).data(window_num)); data = data{1}; data ={data(1+3*(dec-1):3*(dec-1)+3)};
                fraction.kolmogorov_wc_thirds(eeg_num).decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                data = normalize(features.kolmogorov_wc_fifths(eeg_num).data(window_num)); data = data{1}; data = {data(1+5*(dec-1):5*(dec-1)+5)};
                fraction.kolmogorov_wc_fifths(eeg_num).decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                % copy data structures / 6
                % [mean energy of wc whole/thirds/fifths decomposition by decomposition]
                data = normalize(features.mean_energy_wc_whole(eeg_num). data(window_num)); data = data{1}; data ={data(1+(dec-1):(dec-1)+1)};
                fraction.mean_energy_wc_whole(eeg_num). decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                data = normalize(features.mean_energy_wc_thirds(eeg_num).data(window_num)); data = data{1}; data ={data(1+3*(dec-1):3*(dec-1)+3)};
                fraction.mean_energy_wc_thirds(eeg_num).decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                data = normalize(features.mean_energy_wc_fifths(eeg_num).data(window_num)); data = data{1}; data = {data(1+5*(dec-1):5*(dec-1)+5)};
                fraction.mean_energy_wc_fifths(eeg_num).decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
            end
            % assign class
            fraction.eeg(eeg_num).label(next_window_fraction_index(eeg_num)) = word_class;
            % decrease number of needed 'attack' examples
            total_words_in_fraction_attack = total_words_in_fraction_attack - 1;
            next_window_fraction_index(eeg_num) = next_window_fraction_index(eeg_num) + 1;
            
        % if we need more non attacks in subset, take it
        elseif (total_words_in_fraction_no_attack > 0 && word_class==0)
            % copy data structures / 1 
            % [entropy of signal whole/thirds/fifths]
            fraction.entropy_signal_whole(eeg_num). data(next_window_fraction_index(eeg_num)) = features.entropy_signal_whole(eeg_num).data(window_num);
            fraction.entropy_signal_thirds(eeg_num).data(next_window_fraction_index(eeg_num)) = normalize(features.entropy_signal_thirds(eeg_num).data(window_num));
            fraction.entropy_signal_fifths(eeg_num).data(next_window_fraction_index(eeg_num)) = normalize(features.entropy_signal_fifths(eeg_num).data(window_num));
            % copy data structures / 2
            % [kolmogorov of signal whole/thirds/fifths]
            fraction.kolmogorov_signal_whole(eeg_num). data(next_window_fraction_index(eeg_num)) = features.kolmogorov_signal_whole(eeg_num). data(window_num);
            fraction.kolmogorov_signal_thirds(eeg_num).data(next_window_fraction_index(eeg_num)) = normalize(features.kolmogorov_signal_thirds(eeg_num).data(window_num));
            fraction.kolmogorov_signal_fifths(eeg_num).data(next_window_fraction_index(eeg_num)) = normalize(features.kolmogorov_signal_fifths(eeg_num).data(window_num));
            % copy data structure / 3
            % [mean energy of signal whole/thirds/fifths]
            fraction.mean_energy_signal_whole(eeg_num). data(next_window_fraction_index(eeg_num)) = features.mean_energy_signal_whole(eeg_num). data(window_num);
            fraction.mean_energy_signal_thirds(eeg_num).data(next_window_fraction_index(eeg_num)) = normalize(features.mean_energy_signal_thirds(eeg_num).data(window_num));
            fraction.mean_energy_signal_fifths(eeg_num).data(next_window_fraction_index(eeg_num)) = normalize(features.mean_energy_signal_fifths(eeg_num).data(window_num));
            for dec=1:8
                % copy data structures / 4
                % [entropy of wc whole/thirds/fifths, decomposition by decomposition]
                data = normalize(features.entropy_wc_whole(eeg_num). data(window_num)); data = data{1}; data ={data(1+(dec-1):(dec-1)+1)};
                fraction.entropy_wc_whole(eeg_num). decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                data = normalize(features.entropy_wc_thirds(eeg_num).data(window_num)); data = data{1}; data ={data(1+3*(dec-1):3*(dec-1)+3)};
                fraction.entropy_wc_thirds(eeg_num).decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                data = normalize(features.entropy_wc_fifths(eeg_num).data(window_num)); data = data{1}; data = {data(1+5*(dec-1):5*(dec-1)+5)};
                fraction.entropy_wc_fifths(eeg_num).decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                % copy data structures / 5
                % [kolmogorov of wc whole/thirds/fifths, decomposition by decomposition]
                data = normalize(features.kolmogorov_wc_whole(eeg_num). data(window_num)); data = data{1}; data ={data(1+(dec-1):(dec-1)+1)};
                fraction.kolmogorov_wc_whole(eeg_num). decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                data = normalize(features.kolmogorov_wc_thirds(eeg_num).data(window_num)); data = data{1}; data ={data(1+3*(dec-1):3*(dec-1)+3)};
                fraction.kolmogorov_wc_thirds(eeg_num).decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                data = normalize(features.kolmogorov_wc_fifths(eeg_num).data(window_num)); data = data{1}; data = {data(1+5*(dec-1):5*(dec-1)+5)};
                fraction.kolmogorov_wc_fifths(eeg_num).decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                % copy data structures / 6
                % [mean energy of wc whole/thirds/fifths decomposition by decomposition]
                data = normalize(features.mean_energy_wc_whole(eeg_num). data(window_num)); data = data{1}; data ={data(1+(dec-1):(dec-1)+1)};
                fraction.mean_energy_wc_whole(eeg_num). decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                data = normalize(features.mean_energy_wc_thirds(eeg_num).data(window_num)); data = data{1}; data ={data(1+3*(dec-1):3*(dec-1)+3)};
                fraction.mean_energy_wc_thirds(eeg_num).decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
                data = normalize(features.mean_energy_wc_fifths(eeg_num).data(window_num)); data = data{1}; data = {data(1+5*(dec-1):5*(dec-1)+5)};
                fraction.mean_energy_wc_fifths(eeg_num).decomposition_num(dec).data(next_window_fraction_index(eeg_num)) = data;
            end
            % assign class
            fraction.eeg(eeg_num).label(next_window_fraction_index(eeg_num)) = word_class;
            % decrease number of needed 'attack' examples
            total_words_in_fraction_no_attack = total_words_in_fraction_no_attack - 1;
            next_window_fraction_index(eeg_num) = next_window_fraction_index(eeg_num) + 1;
        % the remaining words are for the remaining structure
        else
            % copy data structures / 1 
            % [entropy of signal whole/thirds/fifths]
            remaining.entropy_signal_whole(eeg_num). data(next_window_remaining_index(eeg_num)) = features.entropy_signal_whole(eeg_num).data(window_num);
            remaining.entropy_signal_thirds(eeg_num).data(next_window_remaining_index(eeg_num)) = normalize(features.entropy_signal_thirds(eeg_num).data(window_num));
            remaining.entropy_signal_fifths(eeg_num).data(next_window_remaining_index(eeg_num)) = normalize(features.entropy_signal_fifths(eeg_num).data(window_num));
            % copy data structures / 2
            % [kolmogorov of signal whole/thirds/fifths]
            remaining.kolmogorov_signal_whole(eeg_num). data(next_window_remaining_index(eeg_num)) = features.kolmogorov_signal_whole(eeg_num). data(window_num);
            remaining.kolmogorov_signal_thirds(eeg_num).data(next_window_remaining_index(eeg_num)) = normalize(features.kolmogorov_signal_thirds(eeg_num).data(window_num));
            remaining.kolmogorov_signal_fifths(eeg_num).data(next_window_remaining_index(eeg_num)) = normalize(features.kolmogorov_signal_fifths(eeg_num).data(window_num));
            % copy data structure / 3
            % [mean energy of signal whole/thirds/fifths]
            remaining.mean_energy_signal_whole(eeg_num). data(next_window_remaining_index(eeg_num)) = features.mean_energy_signal_whole(eeg_num). data(window_num);
            remaining.mean_energy_signal_thirds(eeg_num).data(next_window_remaining_index(eeg_num)) = normalize(features.mean_energy_signal_thirds(eeg_num).data(window_num));
            remaining.mean_energy_signal_fifths(eeg_num).data(next_window_remaining_index(eeg_num)) = normalize(features.mean_energy_signal_fifths(eeg_num).data(window_num));
            for dec=1:8
                % copy data structures / 4
                % [entropy of wc hwole/thirds/fifths, decomposition by decomposition]
                data = normalize(features.entropy_wc_whole(eeg_num). data(window_num)); data = data{1}; data ={data(1+(dec-1):(dec-1)+1)};
                remaining.entropy_wc_whole(eeg_num). decomposition_num(dec).data(next_window_remaining_index(eeg_num)) = data;
                data = normalize(features.entropy_wc_thirds(eeg_num).data(window_num)); data = data{1}; data ={data(1+3*(dec-1):3*(dec-1)+3)};
                remaining.entropy_wc_thirds(eeg_num).decomposition_num(dec).data(next_window_remaining_index(eeg_num)) = data;
                data = normalize(features.entropy_wc_fifths(eeg_num).data(window_num)); data = data{1}; data = {data(1+5*(dec-1):5*(dec-1)+5)};
                remaining.entropy_wc_fifths(eeg_num).decomposition_num(dec).data(next_window_remaining_index(eeg_num)) = data;
                % copy data structures / 5
                % [kolmogorov of wc whole/thirds/fifths, decomposition by decomposition]
                data = normalize(features.kolmogorov_wc_whole(eeg_num). data(window_num)); data = data{1}; data ={data(1+(dec-1):(dec-1)+1)};
                remaining.kolmogorov_wc_whole(eeg_num). decomposition_num(dec).data(next_window_remaining_index(eeg_num)) = data;
                data = normalize(features.kolmogorov_wc_thirds(eeg_num).data(window_num)); data = data{1}; data ={data(1+3*(dec-1):3*(dec-1)+3)};
                remaining.kolmogorov_wc_thirds(eeg_num).decomposition_num(dec).data(next_window_remaining_index(eeg_num)) = data;
                data = normalize(features.kolmogorov_wc_fifths(eeg_num).data(window_num)); data = data{1}; data = {data(1+5*(dec-1):5*(dec-1)+5)};
                remaining.kolmogorov_wc_fifths(eeg_num).decomposition_num(dec).data(next_window_remaining_index(eeg_num)) = data;
                % copy data structures / 6
                % [mean energy of wc whole/thirds/fifths decomposition by decomposition]
                data = normalize(features.mean_energy_wc_whole(eeg_num). data(window_num)); data = data{1}; data ={data(1+(dec-1):(dec-1)+1)};
                remaining.mean_energy_wc_whole(eeg_num). decomposition_num(dec).data(next_window_remaining_index(eeg_num)) = data;
                data = normalize(features.mean_energy_wc_thirds(eeg_num).data(window_num)); data = data{1}; data ={data(1+3*(dec-1):3*(dec-1)+3)};
                remaining.mean_energy_wc_thirds(eeg_num).decomposition_num(dec).data(next_window_remaining_index(eeg_num)) = data;
                data = normalize(features.mean_energy_wc_fifths(eeg_num).data(window_num)); data = data{1}; data = {data(1+5*(dec-1):5*(dec-1)+5)};
                remaining.mean_energy_wc_fifths(eeg_num).decomposition_num(dec).data(next_window_remaining_index(eeg_num)) = data;
            end
            % assign class
            remaining.eeg(eeg_num).label(next_window_remaining_index(eeg_num)) = word_class;
            % update the index of the next window
            next_window_remaining_index(eeg_num) = next_window_remaining_index(eeg_num) + 1;
        end
    end
end

function [out] = normalize(vector)
    out{:} = vector{:}./sqrt(sum(vector{:}.^2));
end

