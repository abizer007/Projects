import random
import time
from colorama import Fore, Style  # Make sure to install colorama (pip install colorama)

MAX_LINES = 3
MAX_BET = 100
MIN_BET = 1

ROWS = 3
COLS = 3

symbol_count = {
    "A": 2,
    "B": 4,
    "C": 6,
    "D": 8,
    "E": 3,
    "F": 5,
    "G": 7,
}

# Color codes for symbols (using colorama)
symbol_colors = {
    "A": Fore.RED,
    "B": Fore.GREEN,
    "C": Fore.BLUE,
    "D": Fore.YELLOW,
    "E": Fore.MAGENTA,
    "F": Fore.CYAN,
    "G": Fore.WHITE,
}

def get_slot_machine_spin(rows, cols, symbols):
    all_symbols = []
    for symbol, symbol_count in symbols.items():
        for _ in range(symbol_count):
            all_symbols.append(symbol)
    columns = []
    for _ in range(cols):
        column = []
        current_symbols = all_symbols[:]
        for _ in range(rows):
            value = random.choice(current_symbols)
            current_symbols.remove(value)
            column.append(value)
        columns.append(column)

    return columns

def print_slot_machine(columns):
    print(Style.BRIGHT + "🎰 Slot Machine 🎰" + Style.RESET_ALL)
    for row in range(len(columns[0])):
        for i, column in enumerate(columns):
            if i != len(columns) - 1:
                print(symbol_colors[column[row]] + column[row] + Style.RESET_ALL, end=" | ")
            else:
                print(symbol_colors[column[row]] + column[row] + Style.RESET_ALL, end="")
        print()

def deposit():
    while True:
        amount = input("Enter deposit amount: $")
        if amount.isdigit():
            amount = int(amount)
            if amount > 0:
                break
            else:
                print("Amount must be greater than 0.")
        else:
            print("Please enter a number.")
    return amount

def get_number_of_lines():
    while True:
        lines = input(f"Enter the number of lines to bet on (1-{MAX_LINES}): ")
        if lines.isdigit():
            lines = int(lines)
            if 1 <= lines <= MAX_LINES:
                break
            else:
                print("Enter a valid number of lines.")
        else:
            print("Please enter a number.")
    return lines

def get_bet(balance):
    while True:
        amount = input(f"What would you like to bet? (Min: {MIN_BET}, Max: {min(balance, MAX_BET)}): $")
        if amount.isdigit():
            amount = int(amount)
            if MIN_BET <= amount <= min(balance, MAX_BET):
                break
            else:
                print(f"Amount must be between ${MIN_BET} - ${min(balance, MAX_BET)}.")
        else:
            print("Please enter a number.")
    return amount

def display_results(result):
    time.sleep(1)
    print("\nResults:")
    for line, symbols in result.items():
        result_line = f"Line {line}: "
        for symbol in symbols:
            result_line += symbol_colors[symbol] + symbol + Style.RESET_ALL + " | "
        print(result_line[:-2])  # Remove the last separator
    print()

def calculate_winnings(result, bet):
    winnings = 0
    for line, symbols in result.items():
        unique_symbols = set(symbols)
        if len(unique_symbols) == 1:  # All symbols on the line are the same
            winnings += symbol_count[unique_symbols.pop()] * bet
    return winnings

def main():
    print("Welcome to the Fancy Slot Machine!\n")
    balance = deposit()
    while balance > 0:
        lines = get_number_of_lines()
        bet = get_bet(balance)

        # Spin the slot machine
        print("\nSpinning the reels...")
        time.sleep(2)  # Simulate spinning with a delay
        slots = get_slot_machine_spin(ROWS, COLS, symbol_count)
        print_slot_machine(slots)

        # Display results
        result = {line + 1: [slots[row][line] for row in range(ROWS)] for line in range(lines)}
        display_results(result)

        # Calculate winnings
        winnings = calculate_winnings(result, bet)
        if winnings > 0:
            print(f"Congratulations! You won ${winnings}!")
        else:
            print("Sorry, no winnings this time.")

        # Update balance
        balance += winnings - bet
        print(f"Your current balance: ${balance}\n")

        # Ask if the player wants to play again
        play_again = input("Do you want to play again? (yes/no): ").lower()
        if play_again != 'yes':
            print("Thanks for playing! Goodbye!")
            break

if __name__ == "__main__":
    main()
