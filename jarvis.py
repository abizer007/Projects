import speech_recognition as sr
import pyttsx3
import wikipedia
import datetime
import webbrowser
import random
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.naive_bayes import MultinomialNB

# ------------------- Training Data ---------------------
training_data = [
    {"input": "tell me a joke", "intent": "joke"},
    {"input": "make me laugh", "intent": "joke"},
    {"input": "what's the time", "intent": "time"},
    {"input": "what time is it", "intent": "time"},
    {"input": "search for something", "intent": "search"},
    {"input": "look it up", "intent": "search"},
    {"input": "play some music", "intent": "music"},
    {"input": "open a website", "intent": "open_website"},
    {"input": "weather report", "intent": "weather"},
    {"input": "how is the weather", "intent": "weather"}
]

# ------------------- ML Training -----------------------
vectorizer = CountVectorizer()
X_train = vectorizer.fit_transform([item['input'] for item in training_data])
y_train = [item['intent'] for item in training_data]
model = MultinomialNB()
model.fit(X_train, y_train)

def predict_intent(query):
    if not query.strip():
        return "unknown"
    query_vectorized = vectorizer.transform([query])
    intent = model.predict(query_vectorized)
    return intent[0]

# ------------------- Voice & Engine --------------------
recognizer = sr.Recognizer()
engine = pyttsx3.init()
engine.setProperty('rate', 160)
engine.setProperty('voice', engine.getProperty('voices')[1].id)

def speak(text):
    print("JARVIS:", text)
    engine.say(text)
    engine.runAndWait()

def listen():
    with sr.Microphone() as source:
        print("🎙 Listening...")
        recognizer.adjust_for_ambient_noise(source)
        audio = recognizer.listen(source)

    try:
        query = recognizer.recognize_google(audio)
        print(f"User: {query}")
        return query.lower()
    except sr.UnknownValueError:
        speak("Sorry, I didn't catch that.")
        return ""
    except sr.RequestError:
        speak("Speech recognition service is unavailable.")
        return ""

# ------------------- Feature Functions -----------------
def search_wikipedia(query):
    try:
        result = wikipedia.summary(query, sentences=2)
        speak(f"According to Wikipedia: {result}")
    except wikipedia.DisambiguationError:
        speak("There are multiple results. Please be more specific.")
    except wikipedia.PageError:
        speak("I couldn't find any information on that topic.")

def play_music():
    speak("Playing your favorite music.")
    # Placeholder: you can add file playback logic using `os` or `pygame`

def weather_report():
    speak("The weather today is sunny with a high of 25 degrees Celsius.")  # Replace with real API call

def tell_joke():
    jokes = [
        "Why don't scientists trust atoms? Because they make up everything!",
        "Parallel lines have so much in common. It's a shame they'll never meet.",
        "I told my computer I needed a break, and it said 'no problem, I'll go to sleep.'"
    ]
    speak(random.choice(jokes))

def get_time():
    current_time = datetime.datetime.now().strftime("%H:%M")
    speak(f"The current time is {current_time}")

def open_website(query):
    if "youtube" in query:
        webbrowser.open("https://youtube.com")
        speak("Opening YouTube")
    elif "google" in query:
        webbrowser.open("https://google.com")
        speak("Opening Google")
    else:
        webbrowser.open("https://" + query)
        speak(f"Opening {query}")

# ------------------- Main Assistant Loop ----------------
def jarvis():
    speak("Hello, I am JARVIS. How can I assist you today?")

    while True:
        command = listen()

        if "stop" in command or "exit" in command or "goodbye" in command:
            speak("Goodbye! Have a great day!")
            break

        intent = predict_intent(command)

        if intent == "joke":
            tell_joke()
        elif intent == "time":
            get_time()
        elif intent == "search":
            speak("What would you like to search for?")
            query = listen()
            if query:
                search_wikipedia(query)
        elif intent == "music":
            play_music()
        elif intent == "weather":
            weather_report()
        elif intent == "open_website":
            speak("Which website?")
            website_query = listen()
            if website_query:
                open_website(website_query)
        else:
            speak("I'm not sure how to help with that yet.")

# ------------------- Run -------------------------------
if __name__ == "__main__":
    jarvis()
