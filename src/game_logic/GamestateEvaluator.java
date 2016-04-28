/*
 * Copyright (C) 2016 Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package game_logic;

import structures.PlayerEnum;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class GamestateEvaluator {

    private final int transitionsToWin;

    public GamestateEvaluator(int numberAlignToWin) {
        this.transitionsToWin = numberAlignToWin - 1;
    }

    public PlayerEnum determineWinner(PlayerEnum[][] gamestate) {

        int transitionsCount;
        int gridSizeX = gamestate.length;
        int gridSizeY = gamestate[0].length;

        // horizontal
        for (int y = 0; y < gridSizeY; y++) {
            transitionsCount = 0;
            for (int i = 1; i < gridSizeX; i++) {
                PlayerEnum previous = gamestate[i - 1][y];
                PlayerEnum current = gamestate[i][y];

                transitionsCount = manageTransitions(current, previous, transitionsCount);
                if (transitionsCount == transitionsToWin) {
                    return current;
                }
            }
        }

        // vertical
        for (int x = 0; x < gridSizeX; x++) {
            transitionsCount = 0;
            for (int j = 1; j < gridSizeY; j++) {
                PlayerEnum previous = gamestate[j - 1][x];
                PlayerEnum current = gamestate[j][x];

                transitionsCount = manageTransitions(current, previous, transitionsCount);
                if (transitionsCount == transitionsToWin) {
                    return current;
                }
            }
        }

        // diagonal (bottomLeft => topRight)
        for (int y = 0; y < gridSizeY; y++) {
            transitionsCount = 0;
            // x = y
            for (int i = 1; i < y; i++) {
                int j = y - i;

                PlayerEnum previous = gamestate[i - 1][j + 1];
                PlayerEnum current = gamestate[i][j];

                transitionsCount = manageTransitions(current, previous, transitionsCount);
                if (transitionsCount == transitionsToWin) {
                    return current;
                }
            }
        }

        // diagonal (bottomRight => topLeft)
        for (int x = 0; x < gridSizeX; x++) {
            transitionsCount = 0;
            // y = x
            for (int j = gridSizeY-2; j >= 0; j--) {
                int i = j - x;

                PlayerEnum previous = gamestate[i + 1][j + 1];
                PlayerEnum current = gamestate[i][j];

                transitionsCount = manageTransitions(current, previous, transitionsCount);
                if (transitionsCount == transitionsToWin) {
                    return current;
                }
            }
        }
        
        // TODO : Attention, ne marche que pour des grilles carré 3x3 pour le moment, il faut rajouter 2 boucles pour gérer tous les cas de diagonales

        return PlayerEnum.NOTHING;
    }

    private int manageTransitions(PlayerEnum current, PlayerEnum previous, int actualTransitionsCount) {
        if (current != PlayerEnum.NOTHING && previous.equals(current)) {
            actualTransitionsCount++;
        } else {
            actualTransitionsCount = 0;
        }
        return actualTransitionsCount;
    }
    
    public void displayGamestate(PlayerEnum[][] gamestate, PlayerEnum winner) {
        
        int gridWidth = gamestate.length;
        int gridHeight = gamestate[0].length;
        
        for(int x = 0; x < gridWidth; x++) {
            for(int y = 0; y < gridHeight; y++) {
                
                switch(gamestate[x][y]) {
                    
                    case CIRCLE:
                    System.out.print('O');
                    break;
                    
                    case CROSS:
                    System.out.print('X');
                    break;
                    
                    case NOTHING:
                    System.out.print(' ');
                    break;
                }
            }
            System.out.print('\n');
        }
            
        if(winner == PlayerEnum.NOTHING) {
            System.out.println("The game is in progress.");
        }
        else
            System.out.println("The player "+winner+" is the winner");
    }

}
