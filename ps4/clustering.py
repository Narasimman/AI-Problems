import re
import math
import sys
from porterstemmer import PorterStemmer

class Clustering:
  def __init__(self):
    self.texts = {}
    self.wordsList = {}
    self.porter = PorterStemmer()
    self.input = open("corpus.txt")
    self.wordsWeight = {}
    self.index_to_author = {}

  def parse_input(self):
    blank = True
    bio = ""
    index = 0
    #FIX ME: reads the input file and creates a map of author=>bio
    for line in self.input.readlines():
      line = line.strip()
      if line:
        line = line.lower()
        if blank:
          author = line
          self.index_to_author[index] = author
          index +=1
          blank = False
        else:
          bio += line + " "
      else:
          if bio != "" and author != "":
            self.texts[author] = bio
            author = ""
            bio = ""
            blank = True
    self.texts[author] = bio

    #read the words into a list
    for key in self.texts:
      words = re.sub("[^\w]", " ",  self.texts[key].lower()).split()
      self.wordsList[key] = words

  def preprocess(self, N):
    stopwordsinput = open("stopwords.txt")
    stopwords = stopwordsinput.read()

    #discard all stop words and any word with length > 2    
    for key in self.wordsList:
      words = set()
      for word in self.wordsList[key]:
        if (word not in stopwords):
          if(len(word) > 2):
            words.add(word)
            self.wordsList[key] = words

    text_length = len(self.texts)

    for k in self.wordsList:
      porter_words = set()
      for word in self.wordsList[k]:
        porter_words.add(self.porter.stem(word, 0, len(word)-1))
      self.wordsList[k] = porter_words

    #get the count of each word
    words_reg = {}
    for key in self.wordsList:
      for word in self.wordsList[key]:
        try:
          words_reg[word] += 1
        except:
          words_reg[word] = 1
    
    #discard all words that occur more than half the self.texts
    words_to_remove = []
    for k in words_reg.keys():
      if words_reg[k] > text_length/2:
        words_to_remove.append(k)
        
    for key in self.wordsList:
      words = set()
      for word in self.wordsList[key]:
        if word not in words_to_remove:
          words.add(word)
      self.wordsList[key] = words

    #Calculate the weight
    self.wordsWeight = {}
    for word in words_reg:
      self.wordsWeight[word] = -(math.log(float(words_reg[word])/float(text_length), 2))


    for i in range(text_length):
      for j in range(i):
        set1 = self.wordsList[self.index_to_author[i]]
        set2 = self.wordsList[self.index_to_author[j]]

        common = set1&set2
        if len(common) > 0:
          total_weight = 0
          for word in common:
            total_weight += self.wordsWeight[word]
            if total_weight > float(N):
              print self.index_to_author[i], self.index_to_author[j],total_weight
            else:
              continue

if __name__ == "__main__":
  if len(sys.argv) < 2:
    sys.exit(-1)
  N = sys.argv[1]
  cluster = Clustering()
  cluster.parse_input()
  cluster.preprocess(N)

