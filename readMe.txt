There are two projects:0neTag for data processing and SVDPP for modelling.

OneTag:
1. Run driver/PreDriver.java
   Tips:
   (1) After file initalising( DataPreprocessing.java), the output should be stemmed by stemword.py(in Python Version) and the path should be configured.
   (2) For shoes Amazon dataset, open the output file in DataPreprocessing.java and then convert the first and second column. 
   (3) firstDataProcessing.java is not used in dataset shoes Amazon BUT it is used for processing yelp. If used, Confog.stemmedInputFile must be changed as Confog.stemmedOutputFile in onlyReview.java) 
   
2. Run driver/Driver.java 
   Tips:
   (1) In LdaGibbsSampling.java, we need to pre-configure setting in data/LdaParameter/LdaParameters.txt
   (2) In onlyReview.java, re-configure arraylist settings according to the number of topic
   (3) Original u-i-r should be processed by EXCEL to get user-id, item-id and rating only

SVPP:
1. Go to fileprocessing/SeperateTrainTest.java to define training data and testing data
2. Tip in file processing:
   Recent_trainFile should be extracted by EXCEL.
   Procedure:
     (1)Open rawFile(uid-iid-r-t.txt)and then attached with "Date". "Date" could be found in the inital data file.
     (2)Sort them by "Date" and get the recent XX% as the recent_tranFile
3. Go to driver/Driver_Run.java to run, intialise the model you would like to run

   