import random

def is_prime(num):
    if num < 2:
        return False
    for i in range(2, int(num**0.5) + 1):
        if num % i == 0:
            return False
    return True

def miller_rabin_test(n, k=5):
    if n == 2 or n == 3:
        return True
    if n % 2 == 0:
        return False

    r, s = 0, n - 1
    while s % 2 == 0:
        r += 1
        s //= 2

    for _ in range(k):
        a = random.randint(2, n - 2)
        x = pow(a, s, n)
        if x == 1 or x == n - 1:
            continue

        for _ in range(r - 1):
            x = pow(x, 2, n)
            if x == n - 1:
                break
        else:
            return False

    return True

def generate_perfect_numbers(count):
    perfect_numbers = []
    p = 2  # starting with the first prime exponent

    while len(perfect_numbers) < count:
        prime_candidate = 2**p - 1

        if is_prime(prime_candidate) and miller_rabin_test(prime_candidate):
            perfect_number = 2**(p-1) * prime_candidate
            perfect_numbers.append(perfect_number)

        p += 1

    return perfect_numbers

# Input number of perfect numbers
num_of_perfect_numbers = int(input("Enter the number of perfect numbers you want: "))

# Generate and print perfect numbers
result = generate_perfect_numbers(num_of_perfect_numbers)
print(f"The first {num_of_perfect_numbers} perfect numbers are: {result}")
