% Open the text file
fid = fopen('simulation.txt', 'r');

% Initialize the counters
tie_count = 0;
player_count = 0;
computer_count = 0;

% Read the text file line by line
tline = fgetl(fid);
while ischar(tline)
    
    % Check if the line contains the word "Tie"
    if contains(tline, 'Tie')
        tie_count = tie_count + 1;
    end
    
    % Check if the line contains the word "Player"
    if contains(tline, 'Player')
        player_count = player_count + 1;
    end
    
    % Check if the line contains the word "Computer"
    if contains(tline, 'Computer')
        computer_count = computer_count + 1;
    end
    
    % Read the next line
    tline = fgetl(fid);
end

% Close the text file
fclose(fid);

% Print the percentages
fprintf('-------------------------------------\n');
fprintf('Ties: %d\n', tie_count);
fprintf('Player Wins: %d\n', player_count);
fprintf('Computer Wins: %d\n', computer_count);
fprintf('-------------------------------------\n');
fprintf('Total: %d\n', (tie_count+player_count+computer_count));
fprintf('-------------------------------------\n');
fprintf('Chance Of Tie: %0.3f%%\n', (tie_count/(tie_count+player_count+computer_count))*100);
fprintf('Chance Of Player Win: %0.3f%%\n', (player_count/(tie_count+player_count+computer_count))*100);
fprintf('Chance Of Computer Win: %0.3f%%\n', (computer_count/(tie_count+player_count+computer_count))*100);
fprintf('-------------------------------------\n');
