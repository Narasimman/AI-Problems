import re
import math
import sys

def parse_input(N):
  input = open("corpus.txt")
  stopwordsinput = open("stopwords.txt")
  stopwords = stopwordsinput.read()

  wordsList = []
  texts = {}
  blank = True
  bio = ""

  #FIX ME: reads the input file and creates a map of author=>bio
  for str in input.readlines():
    str = str.strip()
    if str:
      str = str.lower()
      if blank:
        author = str
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
  for str in texts.values():
    words = re.sub("[^\w]", " ",  str.lower()).split()
    wordsList.extend(words)

  words = set()

  #discard all stop words and any word with length > 2    
  for word in wordsList:
    if (word not in stopwords):
      if(len(word) > 2):
        words.add(word)

  #get the count of each word
  words_reg = {}
  words_to_text = {}
  words_in_list = []
  for word in words:
    index = 0
    words_in_list = []
    for biotext in texts.values():
      index +=1
 
      if (word in biotext):
        words_in_list.append(index)
        try:
          words_reg[word] += 1
        except:
          words_reg[word] = 1
    #get the list in word in a map
    words_to_text[word] = words_in_list

  text_length = len(texts)

  #discard all words that occur more than half the texts
  for k in words_reg.keys():
    if words_reg[k] > text_length/2:
      words_reg.remove(k)
  
  #Calculate the weight
  words_weight = {}
  for word in words_reg:
    words_weight[word] = -(math.log(float(words_reg[word])/float(text_length), 2))

  f = open('potter_input','w')
  for k in words_weight.keys():
    f.write(k + " ") # python will convert \n to os.linesep
    #print "%s: %f" % (k, words_weight[k])
  f.close() # you can omit in most cases as the destructor will call it
        
if __name__ == "__main__":
  if len(sys.argv) < 2:
    sys.exit(-1)
  N = sys.argv[1]
  print N
  parse_input(N)

