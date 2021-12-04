We did not ask for the technical help because this project is pretty good. Writing extensible plugins don't require changes to other classes. Besides, the tests are complete for us to make new plugins.

A problem we faced at the beginning is to start the program. We faced Expected linebreaks to be 'LF' but found 'CRLF' error. After googling, we found out the reason was we used WindowsOS and the program might be developed under MacOS. So we had to change linebreaks to be 'LF' manually, then the program started to run.

One tough thing we met to implement data plugin is to find free stock api. We did not find a proper java api, so we use python api to save the results.
