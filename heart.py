import turtle
import time

def draw_heart():
    heart.begin_fill()
    heart.fillcolor("red")
    heart.pencolor("white")  # Set the outline color
    heart.left(50)
    heart.forward(133)
    heart.circle(50, 200)
    heart.right(140)
    heart.circle(50, 200)
    heart.forward(133)
    heart.end_fill()

def draw_message(message):
    heart.penup()
    heart.goto(0, -180)
    heart.color("white")
    heart.write(message, align="center", font=("Courier", 24, "bold"))

def pulsate():
    for _ in range(4):
        heart.shapesize(1.2)
        time.sleep(0.2)
        heart.shapesize(1)
        time.sleep(0.2)

# Set up the turtle
screen = turtle.Screen()
screen.bgcolor("black")
screen.title("Heart Animation")

heart = turtle.Turtle()
heart.shape("turtle")
heart.speed(2)
heart.shapesize(1)

# Draw the heart
draw_heart()

# Pulsating effect
pulsate()

# Display the final message
draw_message("I Love You xyz")

# Close the window when clicked
screen.exitonclick()

