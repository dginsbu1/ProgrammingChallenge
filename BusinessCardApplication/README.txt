# BusinessCardAppllication

BusinessCardApplication is a program designed to 

## Usage

From commandline, go into the BusinessCardApplication folder. 
Once inside type runner.bat followed by at least two argments.
arg[0] must be a path to a list of names that the program will use with one name(first or last) per line
       Alternatively, type "default" to use a prefined shorter list of names.
arg[1]-arg[n] the paths to the documents containing the contact information.
Note: for testing use example1.txt, example2.txt, and example3.txt

examples:
Default
C:\...\BusinessCard: runner.bat default example1.txt example2.txt
Custom:
C:\...\BusinessCard: runner.bat "C:\Docs\names.txt" BobInfo.txt

Example Output:
Single Input:
C:\...\BusinessCardApplication>runner.bat names.txt example1.txt
BusinessCardApplication:

Entegra Systems
John Doe
Senior Software Engineer
(410)555-1234
john.doe@entegrasystems.com

==>

Name: John Doe
Phone: 4105551234
Email: john.doe@entegrasystems.com
-------------------

Multiple Inputs:

C:\...\BusinessCardApplication>runner.bat default example2.txt example3.txt
BusinessCardApplication:

Acme Technologies
Analytic Developer
Jane Doe
1234 Roadrunner Way
Columbia, MD 12345
Phone: 410-555-1234
Fax: 410-555-4321
Jane.doe@acmetech.com

==>

Name: Jane Doe
Phone: 4105551234
Email: Jane.doe@acmetech.com
-------------------
Bob Smith
Software Engineer
Decision & Security Technologies
ABC Technologies
123 North 11th Street
Suite 229
Arlington, VA 22209
Tel: +1 (703) 555-1259
Fax: +1 (703) 555-1200
bsmith@abctech.com

==>

Name: Bob Smith
Phone: 17035551259
Email: bsmith@abctech.com
-------------------

