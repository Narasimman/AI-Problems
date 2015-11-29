import re

def parse_input():
    input = open("corpus.txt")
    stopwordsinput = open("stopwords.txt")
    stopwords = stopwordsinput.read()
    wordsList = []
    
    #read the words into a list
    for str in input.readlines():
        words = re.sub("[^\w]", " ",  str.lower()).split()
        wordsList.extend(words)

    #discard all stop words and any word with length > 2
    words = []
    for word in wordsList:
        if (word not in stopwords):
            if(len(word) > 2):
                words.append(word)

    #get the count of each word
    d = {}
    for word in words:
        try:
            d[word] += 1
        except:
            d[word] = 1

    # discard all words that appear more than half the number of times
    count = len(words)
    for k in d.keys():
        if d[k] > count/2:
            words.remove(d)
    
    for k in d.keys(): 
        print "%s: %d" % (k, d[k])
    
    print len(words)
    
    #print words

if __name__ == "__main__":
        parse_input()
