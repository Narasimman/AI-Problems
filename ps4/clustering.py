import re
import math
import sys
from porterstemmer import PorterStemmer

class Clustering:
  def __init__(self):
    self.text = {}
    self.wordsList = {}
    self.porter = PorterStemmer()
    self.input = open("corpus.txt")

  def parse_input(self, N):
    stopwordsinput = open("stopwords.txt")
    stopwords = stopwordsinput.read()

    self.texts = {}
    blank = True
    bio = ""
    index_to_author = {}
    index = 0
    #FIX ME: reads the input file and creates a map of author=>bio
    for line in self.input.readlines():
      line = line.strip()
      if line:
        line = line.lower()
        if blank:
          author = line
          index_to_author[index] = author
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

    #discard all stop words and any word with length > 2    
    for key in self.wordsList:
      words = set()
      for word in self.wordsList[key]:
        if (word not in stopwords):
          if(len(word) > 2):
            words.add(word)
            self.wordsList[key] = words

    #get the count of each word
    words_reg = {}
    for key in self.wordsList:
      for word in self.wordsList[key]:
        try:
          words_reg[word] += 1
        except:
          words_reg[word] = 1

    text_length = len(self.texts)

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
    words_weight = {}
    for word in words_reg:
      words_weight[word] = -(math.log(float(words_reg[word])/float(text_length), 2))


    for i in range(text_length):
      for j in range(i):
        set1 = self.wordsList[index_to_author[i]]
        set2 = self.wordsList[index_to_author[j]]

        common = set1&set2
        if len(common) > 0:
          total_weight = 0
          for word in common:
            total_weight += words_weight[word]
            if total_weight > float(N):
              print index_to_author[i],index_to_author[j],total_weight
            else:
              continue

    f = open('potter_input','w')
    for k in self.wordsList:
      for word in self.wordsList[k]:
        f.write(word + " ") # python will convert \n to os.linesep

        #print k, wordsList[k]
    f.close() # you can omit in most cases as the delineuctor will call it

if __name__ == "__main__":
  if len(sys.argv) < 2:
    sys.exit(-1)
  N = sys.argv[1]
  cluster = Clustering()
  cluster.parse_input(N)

