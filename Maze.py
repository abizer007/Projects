import pygame
import random
import sys
import numpy as np
from pathfinding.core.diagonal_movement import DiagonalMovement
from pathfinding.core.grid import Grid as PathGrid
from pathfinding.finder.a_star import AStarFinder

# Initialize pygame
pygame.init()

# Screen dimensions
WIDTH, HEIGHT = 600, 600
CELL_SIZE = 30
ROWS, COLS = WIDTH // CELL_SIZE, HEIGHT // CELL_SIZE

# Colors
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
BLUE = (0, 0, 255)
GREEN = (0, 255, 0)
RED = (255, 0, 0)
YELLOW = (255, 255, 0)

# Set up the screen
screen = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption("Advanced Maze Game")

# Maze symbols
WALL = 1
PATH = 0
TREASURE = 2
EXIT = 3
TRAP = 4

# Player variables
player_pos = [1, 1]
treasures_collected = 0
player_health = 100
player_speed = 1

# Enemy class
class Enemy:
    def __init__(self, x, y, speed):
        self.x = x
        self.y = y
        self.speed = speed
        self.health = 100

    def move(self, player_pos, maze):
        grid = PathGrid(matrix=maze)
        start = grid.node(self.x, self.y)
        end = grid.node(player_pos[1], player_pos[0])
        finder = AStarFinder(diagonal_movement=DiagonalMovement.never)
        path, _ = finder.find_path(start, end, grid)
        if len(path) > 1:
            self.x, self.y = path[1]

    def draw(self):
        pygame.draw.rect(screen, RED, (self.y * CELL_SIZE, self.x * CELL_SIZE, CELL_SIZE, CELL_SIZE))

# Trap class
class Trap:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def trigger(self, player):
        player_health = max(0, player_health - 20)  # Trap reduces health by 20

    def draw(self):
        pygame.draw.rect(screen, BLACK, (self.y * CELL_SIZE, self.x * CELL_SIZE, CELL_SIZE, CELL_SIZE))

# Timer class for level time limit
class Timer:
    def __init__(self, duration):
        self.start_time = pygame.time.get_ticks()
        self.duration = duration

    def time_left(self):
        elapsed = pygame.time.get_ticks() - self.start_time
        return max(0, self.duration - elapsed)

# Generate a random maze
def generate_maze(rows, cols):
    maze = [[WALL for _ in range(cols)] for _ in range(rows)]
    for i in range(1, rows - 1):
        for j in range(1, cols - 1):
            if random.random() > 0.3:
                maze[i][j] = PATH
    
    # Add treasures
    for _ in range(5):
        t_row, t_col = random.randint(1, rows - 2), random.randint(1, cols - 2)
        maze[t_row][t_col] = TREASURE

    # Add traps
    for _ in range(3):
        t_row, t_col = random.randint(1, rows - 2), random.randint(1, cols - 2)
        maze[t_row][t_col] = TRAP

    # Set exit position
    maze[rows - 2][cols - 2] = EXIT

    return maze

# Draw the maze on the screen
def draw_maze(maze):
    for row in range(ROWS):
        for col in range(COLS):
            if maze[row][col] == WALL:
                pygame.draw.rect(screen, BLACK, (col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE))
            elif maze[row][col] == TREASURE:
                pygame.draw.rect(screen, YELLOW, (col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE))
            elif maze[row][col] == EXIT:
                pygame.draw.rect(screen, GREEN, (col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE))
            elif maze[row][col] == TRAP:
                pygame.draw.rect(screen, RED, (col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE))
            else:
                pygame.draw.rect(screen, WHITE, (col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE))

# Draw the player
def draw_player():
    pygame.draw.rect(screen, BLUE, (player_pos[1] * CELL_SIZE, player_pos[0] * CELL_SIZE, CELL_SIZE, CELL_SIZE))

# Handle player movement
def move_player(maze, direction):
    global treasures_collected, player_health
    row, col = player_pos
    if direction == 'UP' and maze[row - 1][col] != WALL:
        player_pos[0] -= player_speed
    elif direction == 'DOWN' and maze[row + 1][col] != WALL:
        player_pos[0] += player_speed
    elif direction == 'LEFT' and maze[row][col - 1] != WALL:
        player_pos[1] -= player_speed
    elif direction == 'RIGHT' and maze[row][col + 1] != WALL:
        player_pos[1] += player_speed
    
    # Collect treasure
    if maze[player_pos[0]][player_pos[1]] == TREASURE:
        treasures_collected += 1
        maze[player_pos[0]][player_pos[1]] = PATH
    
    # Trigger trap
    if maze[player_pos[0]][player_pos[1]] == TRAP:
        player_health -= 20
        maze[player_pos[0]][player_pos[1]] = PATH
    
    # Check for exit
    if maze[player_pos[0]][player_pos[1]] == EXIT:
        return True
    return False

def main():
    global player_health
    maze = generate_maze(ROWS, COLS)
    game_over = False
    timer = Timer(60000)  # 60 seconds per level
    enemies = [Enemy(random.randint(1, ROWS - 2), random.randint(1, COLS - 2), speed=1) for _ in range(2)]
    level_number = 1

    while not game_over:
        screen.fill(WHITE)
        draw_maze(maze)
        draw_player()

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()

            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_UP:
                    game_over = move_player(maze, 'UP')
                elif event.key == pygame.K_DOWN:
                    game_over = move_player(maze, 'DOWN')
                elif event.key == pygame.K_LEFT:
                    game_over = move_player(maze, 'LEFT')
                elif event.key == pygame.K_RIGHT:
                    game_over = move_player(maze, 'RIGHT')

        # Update and draw enemies
        for enemy in enemies:
            enemy.move(player_pos, maze)
            enemy.draw()

        # Display the number of treasures collected and player health
        font = pygame.font.Font(None, 36)
        text = font.render(f'Treasures: {treasures_collected}', True, RED)
        screen.blit(text, (10, 10))
        health_text = font.render(f'Health: {player_health}', True, RED)
        screen.blit(health_text, (10, 40))

        pygame.display.flip()

        # Check if player health reaches zero
        if player_health <= 0 or timer.time_left() <= 0:
            game_over = True
    
    # Show win/lose message
    screen.fill(WHITE)
    font = pygame.font.Font(None, 72)
    if player_health > 0:
        text = font.render('You Win!', True, GREEN)
    else:
        text = font.render('Game Over!', True, RED)
    screen.blit(text, (WIDTH // 4, HEIGHT // 2))
    pygame.display.flip()
    pygame.time.wait(3000)

if __name__ == "__main__":
    main()
