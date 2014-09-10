#Word Stemming by Ziyuan Liu
#Install the Stemming package first: https://pypi.python.org/pypi/stemming/1.0

from stemming.porter2 import stem

#Input File
f = open('F:/Project/Processed_Industrial_&_Scientific_review.txt', 'r')

#Output File
g = open('F:/Project/stemmed_Processed_Industrial_&_Scientific_review.txt', 'w')
g.seek(0)
for line in f:
    documents = [stem(word) for word in line.split(" ")]
    documents = " ".join(documents)
    g.writelines(str(documents))

f.close()
g.close()


