import re

def parse_input():
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
        bio += str
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
  d = {}
  for word in words:
    for str in texts.values():
      if (word in str):
        try:
          d[word] += 1
        except:
          d[word] = 1

  for k in d.keys():
    print "%s: %d" % (k, d[k])
        
if __name__ == "__main__":
  parse_input()

