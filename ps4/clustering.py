import re
import math
import sys

def parse_input(N):
  input = open("corpus.txt")
  stopwordsinput = open("stopwords.txt")
  stopwords = stopwordsinput.read()

  wordsList = {}
  texts = {}
  blank = True
  bio = ""
  index_to_author = {}
  index = 0
  #FIX ME: reads the input file and creates a map of author=>bio
  for str in input.readlines():
    str = str.strip()
    if str:
      str = str.lower()
      if blank:
        author = str
        index_to_author[index] = author
        index +=1
        blank = False
      else:
        bio += str + " "
    else:
      if bio != "" and author != "":
        texts[author] = bio
        author = ""
        bio = ""
        blank = True
  texts[author] = bio

  #read the words into a list

  for key in texts:
    words = re.sub("[^\w]", " ",  texts[key].lower()).split()
    wordsList[key] = words


  #discard all stop words and any word with length > 2    
  for key in wordsList:
    words = set()
    for word in wordsList[key]:
      if (word not in stopwords):
        if(len(word) > 2):
          words.add(word)
    wordsList[key] = words

  #get the count of each word
  words_reg = {}
  for key in wordsList:
    for word in wordsList[key]:
      try:
        words_reg[word] += 1
      except:
        words_reg[word] = 1

  text_length = len(texts)

  #discard all words that occur more than half the texts
  words_to_remove = []
  for k in words_reg.keys():
    if words_reg[k] > text_length/2:
      words_to_remove.append(k)
  
  for key in wordsList:
    words = set()
    for word in wordsList[key]:
      if word not in words_to_remove:
        words.add(word)
    wordsList[key] = words

  #Calculate the weight
  words_weight = {}
  for word in words_reg:
    words_weight[word] = -(math.log(float(words_reg[word])/float(text_length), 2))


  for i in range(text_length):
    for j in range(i):
     set1 = wordsList[index_to_author[i]]
     set2 = wordsList[index_to_author[j]]

     common = set1&set2
     if i == 20 and j == 19:
       print common



  f = open('potter_input','w')
  for k in wordsList:
    f.write(k + " ") # python will convert \n to os.linesep
    #print k, wordsList[k]
  f.close() # you can omit in most cases as the destructor will call it

if __name__ == "__main__":
  if len(sys.argv) < 2:
    sys.exit(-1)
  N = sys.argv[1]
  parse_input(N)

