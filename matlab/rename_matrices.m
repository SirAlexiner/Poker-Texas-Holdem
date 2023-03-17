% Specify the mapping between card values and filenames
card_map = containers.Map({'0.0', '0.1', '0.2', '0.3', '0.4', '0.5', '0.6', '0.7', '0.8', '0.9', '1.0', '1.1', '1.2'}, ...
                          {'Two', 'Three', 'Four', 'Five', 'Six', 'Seven', 'Eight', 'Nine', 'Ten', 'Jack', 'Queen', 'King', 'Ace'});

% Specify the input and output directories
input_dir = 'poker_matrix/';
output_dir = 'poker_matrix_renamed/';

% Create the output directory if it doesn't exist
if ~exist(output_dir, 'dir')
    mkdir(output_dir);
end

% Get a list of all the files in the input directory
file_list = dir([input_dir '*.mat']);

% Loop through each file and rename it
for i = 1:length(file_list)
    % Get the filename and split it into parts
    filename = file_list(i).name;
    [~, basename, ext] = fileparts(filename);
    
    % Split the filename into its components
    parts = strsplit(basename, '_');
    y_value = parts{end-1};
    x_value = parts{end};
    
    % Create the new filename using the mapping
    new_filename = sprintf('%s%s_%s.mat', output_dir, card_map(x_value), card_map(y_value));
    
    % Rename the file
    movefile([input_dir filename], new_filename);
end
