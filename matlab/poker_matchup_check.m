output_dir = 'poker_matrix/';

% Specify the range of x and y values
x_values = 0:0.1:1.2;
y_values = 0:0.1:1.2;

% Check if the output directory exists, and create it if it doesn't
if ~exist(output_dir, 'dir')
    mkdir(output_dir);
end

% Check each combination of x and y values
for i = 1:length(x_values)
    for j = 1:length(y_values)
        
        % Specify the output filename
        filename = sprintf('%s%.1f_%.1f.mat', output_dir, x_values(j), y_values(i));
        
        % Check if the file already exists
        if exist(filename, 'file')
            fprintf('File %s already exists, skipping...\n', filename);
            continue;
        end
        
        % Load the player and computer data
        player_data = zeros(13, 8);
        computer_data = zeros(13, 8);
        for k = 0:7
            for l = 0:12
                if isequaln(k,1) || isequaln(k,5)
                    player_data(l+1, k+1) = 20.0 + (40.0 * k) + (l * 2.0) + x_values(i) * 10 + x_values(i);
                    computer_data(l+1, k+1) = 20.0 + (40.0 * k) + (l * 2.0) + y_values(j) * 10 + y_values(j);
                else
                   player_data(l+1, k+1) = 20.0 + (40.0 * k) + (l * 2.0) + x_values(i);
                   computer_data(l+1, k+1) = 20.0 + (40.0 * k) + (l * 2.0) + y_values(j);
                end
            end
        end
        
        % Compare the player and computer data
        poker_matrix = cell(size(player_data, 1), size(player_data, 2), size(computer_data, 1));
        for k = 1:size(player_data, 2)
            for l = 1:size(player_data, 1)
                for m = 1:size(computer_data, 1)
                    if player_data(l,k) < computer_data(m,k)
                        poker_matrix{l,k,m} = [pokerHandToString(k), ' in ', cardRankToString(l), ' # ', num2str(player_data(l,k)), ' Against ', pokerHandToString(k), ' in ', cardRankToString(m), ' & ', num2str(computer_data(m,k)), ' | Computer Wins'];
                    elseif isequaln(player_data(l,k), computer_data(m,k))
                        poker_matrix{l,k,m} = [pokerHandToString(k), ' in ', cardRankToString(l), ' # ', num2str(player_data(l,k)), ' Against ', pokerHandToString(k), ' in ', cardRankToString(m), ' & ', num2str(computer_data(m,k)), ' | Tie'];
                    elseif player_data(l,k) > computer_data(m,k)
                        poker_matrix{l,k,m} = [pokerHandToString(k), ' in ', cardRankToString(l), ' # ', num2str(player_data(l,k)), ' Against ', pokerHandToString(k), ' in ', cardRankToString(m), ' & ', num2str(computer_data(m,k)), ' | Player Wins'];
                    end
                end
            end
        end
        
        % Save the poker matrix to file
        save(filename, 'poker_matrix');
        fprintf('Saved %s\n', filename);
    end
end
run('rename_matrices.m');
run('matrices_to_txt.m');
run('rewrite_poker_matrix_sim.m');
run('poker_win_chance.m');

function str = cardRankToString(rank)
% Convert a card rank integer to its corresponding string representation
% e.g., 1 -> 'A', 11 -> 'J', 13 -> 'K'
    if rank == 13
        str = 'Ace';
    elseif rank == 12
        str = 'King';
    elseif rank == 11
        str = 'Queen';
    elseif rank == 10
        str = 'Jack';
    elseif rank == 9
        str = 'Ten';
    elseif rank == 8
        str = 'Nine';
    elseif rank == 7
        str = 'Eight';
    elseif rank == 6
        str = 'Seven';
    elseif rank == 5
        str = 'Six';
    elseif rank == 4
        str = 'Five';
    elseif rank == 3
        str = 'Four';
    elseif rank == 2
        str = 'Three';
    elseif rank == 1
        str = 'Two';
    end
end

function str = pokerHandToString(hand)
% Convert a poker hand integer to its corresponding string representation
    switch hand
        case 1
            str = 'One Pair';
        case 2
            str = 'Two Pairs';
        case 3
            str = 'Three of a Kind';
        case 4
            str = 'Straight';
        case 5
            str = 'Flush';
        case 6
            str = 'Full House';
        case 7
            str = 'Four of a Kind';
        case 8
            str = 'Straight Flush';
    end
end
