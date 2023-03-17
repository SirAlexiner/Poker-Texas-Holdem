% Get directory path from user
directory_path = 'poker_matrix_renamed\';

% Get list of .mat files in directory
mat_files = dir(fullfile(directory_path, '*.mat'));

% Open output file for writing
output_file = fopen(fullfile(directory_path, 'output.txt'), 'w');

% Loop through each .mat file in directory
for i = 1:numel(mat_files)
    
    % Load .mat file
    mat_file = load(fullfile(directory_path, mat_files(i).name));
    
    % Get list of variable names in .mat file
    variable_names = fieldnames(mat_file);
    
    % Loop through each variable in .mat file
    for j = 1:numel(variable_names)
        
        % Check if variable is a cell array
        if iscell(mat_file.(variable_names{j}))
            
            % Write section title to output file
            [filepath, name, ~] = fileparts(mat_files(i).name);
            fprintf(output_file, '--- (%s) ---\n', name);
            
            % Loop through each cell in variable
            for k = 1:numel(mat_file.(variable_names{j}))
                
                % Write cell contents to output file
                fprintf(output_file, '%s\n', mat_file.(variable_names{j}){k});
                
            end
            
        end
        
    end
    
end

% Close output file
fclose(output_file);