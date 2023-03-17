% Open input and output files
input_file = fopen('poker_matrix_renamed\output.txt', 'r');
output_file = fopen('simulation.txt', 'w');

% Loop over each line in the input file
while ~feof(input_file)
    % Read the current line
    line = fgetl(input_file);
    
    % Check if the line has the expected structure
    if startsWith(line, '--- (') && endsWith(line, ') ---')
        % Extract the Ace values from the line
        ace_left = strsplit(line, {'(', '_', ')'});
        ace_left = ace_left{2};
        ace_right = strsplit(line, {'(', '_', ')'});
        ace_right = ace_right{3};
    elseif startsWith(line, '--- (') && endsWith(line, ') ---')
        
    else
        % Modify the line by replacing "Beats" with "With Ace Beats"
        % and adding the Ace value on both sides
        line = strrep(line, '#', ['With ', ace_left, ' :']);
        line = strrep(line, '&', ['With ', ace_right, ' :']);
    end
    
    % Write the modified line to the output file
    fprintf(output_file, '%s\n', line);
end

% Close input and output files
fclose(input_file);
fclose(output_file);