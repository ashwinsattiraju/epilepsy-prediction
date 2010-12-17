function [performance training_set testing_set] = main_script()
    % Java related code
    javaaddpath('..../java/lamstar-network/bin');
    import ('org.artificial_neural_networks.common.*');
    import ('org.artificial_neural_networks.networks.lamstar.*');
    import ('org.artificial_neural_networks.exceptions.*');
    import ('java.lang.String')
    import ('java.lang.Long')
    import ('java.util.ArrayList')
    
    % load data
    load '../data/chb01/features_0_with_energy.mat';
    %load '../data/chb01/features_15';
    %load '../data/chb01/features_40';
    features = features_0;
    %features(1) = features_15;
    %features(1) = features_40;
    
    % create lamstar manager
    fprintf('LAMSTAR Manager created\n');
    lamstarManager = LamstarFactoryForMatlab();
    
    % add to the manager the features content
    fprintf('LAMSTAR Manager is being loaded with feature set.\n');
    fprintf('\tCreation of training and testing sets...');
    [training_set remaining ] = create_subset_from_features_separated_wc_first(features,  150);
    [testing_set  ~         ] = create_subset_from_features_separated_wc_subsequents(remaining, 50);
    
    % load training and testing features
    fprintf('done.\n\tAdding training features to LAMSTAR Manager...');
    add_features_to_lamstar_for_training(training_set, lamstarManager);
    fprintf('done.\n\tAdding testing features to LAMSTAR Manager...');
    add_features_to_lamstar_for_testing (testing_set,  lamstarManager);
    fprintf('done.\n');
    
    % now initialize network (neuronal distance tolerance, reward, punish)
    fprintf('LAMSTAR Manager is being initialized...');
    lamstarManager.initNewLamstarForMatlab(0.0005, 0.001, -0.001);
    fprintf('done.\n');
    
    % print configuration
    lamstarManager.printLamstarConfiguration();
    
    % train and get results of training
    fprintf('LAMSTAR Manager is being trained (25 iterations, 100%% accuracy, no punishment)...');
    perf = lamstarManager.trainWithOutput(100, 100, false);
    fprintf('done.\nCollecting performances...');
    performance.perf = perf;
    performance.most_important_layer_id = lamstarManager.getIndexOfMostImportantInputLayers();
    figure
    plot(performance.perf);
    fprintf('done.\n');
end