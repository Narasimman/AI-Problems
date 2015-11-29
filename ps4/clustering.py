import re

def parse_input():
    input = open("corpus.txt")
    wordsList = []
    for str in input.readlines():
        words = re.sub("[^\w]", " ",  str).split()
        wordsList.extend(words)
    print wordsList

if __name__ == "__main__":
        parse_input()
