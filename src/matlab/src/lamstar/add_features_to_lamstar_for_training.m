function add_features_to_lamstar_for_training(features, lamstarManager)
    % length of the dataset
    num_of_recs = length(features.entropy_signal_whole);
    num_of_words = 0;
    for i=1:num_of_recs
        num_of_words = num_of_words + length(features.eeg(i).label);
    end
    
    % Featuresets initialization
    % ENTROPY SIGNAL ANALYSIS, 0
    % ESTIMATION OF KOLMOGOROV COMPLEXITY SIGNAL ANALYSIS, 1
    % ENTROPY WAVELET COEFFICIENTS ANALYSIS, 2
    % ESTIMATION OF KOLMOGOROV COMPLEXITY WAVELET COEFFICIENTS ANALYSIS, 3
    
    % traverse all eeg registrations
    counter = 0;
    for i=1:num_of_recs
        % traverse all data
        for j=1:length(features.entropy_signal_whole(i).data);
            % progress notification
            fprintf('Adding data to training set in progress ... %.4g %%\n', counter/num_of_words*100);
            
            % class of word
            c = features.eeg(i).label(j);
            
            % =======================================
            % =========== ENTROPY SIGNAL ============
            % =======================================
            % entropy signal + entropy signal thirds
            % feature_data = [features.entropy_signal_whole(i).data{j} features.entropy_signal_thirds(i).data{j}];
            % lamstarManager.addElementToFeatureSetIDForTraining(0, feature_data, c); 
            
            % entropy signal thirds
            % feature_data = [features.entropy_signal_thirds(i).data{j}];
            % lamstarManager.addElementToFeatureSetIDForTraining(1, feature_data, c); 
            
            % entropy signal fifths
            % feature_data = [features.entropy_signal_fifths(i).data{j}];
            % lamstarManager.addElementToFeatureSetIDForTraining(2, feature_data, c);
            
            % =======================================
            % ========= KOLMOGOROV SIGNAL ===========
            % =======================================
            % kolmogorov signal + kolmogorov signal thirds
            feature_data = [features.kolmogorov_signal_whole(i).data{1} features.kolmogorov_signal_thirds(i).data{1}];
            lamstarManager.addElementToFeatureSetIDForTraining(3, feature_data, c);
            
            % kolmogorov signal thirds
            feature_data = [features.kolmogorov_signal_thirds(i).data{1}];
            lamstarManager.addElementToFeatureSetIDForTraining(4, feature_data, c);
            
            % kolmogorov signal fifths
            feature_data = [features.kolmogorov_signal_fifths(i).data{j}];
            lamstarManager.addElementToFeatureSetIDForTraining(5, feature_data, c);
            
            % ========================================
            % ========= MEAN ENERGY SIGNAL ===========
            % ========================================
            % mean energy signal + kolmogorov signal thirds
            feature_data = [features.mean_energy_signal_whole(i).data{1} features.mean_energy_signal_thirds(i).data{1}];
            lamstarManager.addElementToFeatureSetIDForTraining(6, feature_data, c);
            
            % mean energy signal thirds
            feature_data = [features.mean_energy_signal_thirds(i).data{1}];
            lamstarManager.addElementToFeatureSetIDForTraining(7, feature_data, c);
            
            % mean energy signal fifths
            feature_data = [features.mean_energy_signal_fifths(i).data{j}];
            lamstarManager.addElementToFeatureSetIDForTraining(8, feature_data, c);
            
            for dec=1:8
                % =======================================
                % =========== ENTROPY WAVELET ===========
                % =======================================
                % entropy wavelet whole + thirds (for decomposition dec)
                % feature_data = [features.entropy_wc_whole(i).decomposition_num(dec).data{j} features.entropy_wc_thirds(i).decomposition_num(dec).data{j}];
                % lamstarManager.addElementToFeatureSetIDForTraining(9*dec, feature_data, c);
                
                % entropy wavelet thirds (for decomposition dec)
                % feature_data = [features.entropy_wc_thirds(i).decomposition_num(dec).data{j}];
                % lamstarManager.addElementToFeatureSetIDForTraining(9*dec+1, feature_data, c);
                
                % entropy wavelet fifths (for decomposition dec)
                % feature_data = [features.entropy_wc_fifths(i).decomposition_num(dec).data{j}];
                % lamstarManager.addElementToFeatureSetIDForTraining(9*dec+2, feature_data, c);
                
                % =======================================
                % ========= KOLMOGOROV WAVELET ==========
                % =======================================
                % kolmogorov wavelet whole + thirds (for decomposition dec)
                % feature_data = [features.kolmogorov_wc_whole(i).decomposition_num(dec).data{j} features.kolmogorov_wc_thirds(i).decomposition_num(dec).data{j}];
                % lamstarManager.addElementToFeatureSetIDForTraining(9*dec+3, feature_data, c);
                
                % kolmogorov wavelet thirds (for decomposition dec)
                % feature_data = [features.kolmogorov_wc_thirds(i).decomposition_num(dec).data{j}];
                % lamstarManager.addElementToFeatureSetIDForTraining(9*dec+4, feature_data, c);
                
                % kolmogorov wavelet fifths (for decomposition dec)
                % feature_data = [features.kolmogorov_wc_fifths(i).decomposition_num(dec).data{j}];
                % lamstarManager.addElementToFeatureSetIDForTraining(9*dec+5, feature_data, c);
                
                % ========================================
                % ========= MEAN ENERGY SIGNAL ===========
                % ========================================
                % mean energy of wavelet whole + thirds (for decomposition dec)
                % feature_data = [features.mean_energy_wc_whole(i).decomposition_num(dec).data{j} features.mean_energy_wc_thirds(i).decomposition_num(dec).data{j}];
                % lamstarManager.addElementToFeatureSetIDForTraining(9*dec+6, feature_data, c);
                
                % mean energy of wavelet thirds (for decomposition dec)
                % feature_data = [features.mean_energy_wc_thirds(i).decomposition_num(dec).data{j}];
                % lamstarManager.addElementToFeatureSetIDForTraining(9*dec+7, feature_data, c);
                
                % mean energy of wavelet fifths (for decomposition dec)
                % feature_data = [features.mean_energy_wc_fifths(i).decomposition_num(dec).data{j}];
                % lamstarManager.addElementToFeatureSetIDForTraining(9*dec+8, feature_data, c);
            end
            counter = counter + 1;
        end
    end
end