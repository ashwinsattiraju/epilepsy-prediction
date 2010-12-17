% loads all files ending in data.txt found in folder_path and converts the
% files to eeg_structure
% eeg_out respects this syntax:
%       eeg_structure.
%                  eeg(x).
%                       data = [signals-by-channels matrix]
%                       attack = {true, false}
%                       begin = <int>
%                  sample_frequency = <double>
function [eeg_structure] = format_eeg(folder_path)
    EEG = load(filename, '-ascii');
    EEG = EEG(:, 2:end);
    
end