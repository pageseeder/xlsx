# PAGESEEDER - EXCEL ANT LIBRARY

About this library
------------------

This library provides Apache ANT tasks to:
- import a Microsoft Excel spreadsheet into PageSeeder documents
- export PageSeeder documents into Microsoft Excel spreadsheet

Only PowerPoint presentation from Office 2007 and later are supported.

Dependencies
------------

This library should only depend on ANT 1.8.1.
It does also require an XSLT2 processor (For example Saxon)

Testing
-------

If you want to test the code, ensure that you add the following to the ANT classpath:
- Saxon (available in text/lib)
- Either a version of the jar or both the /src and /classes folders


Copyright (c) 1999-2012 Weborganic Pty Ltd - All Rights Reserved.

## Cell Format

The attribute ss:c/@s is zero-based style index of cell format ss:styleSheet/ss:cellXfs/ss:xf. 

Currently, the cell format has few formats setup however it will only treat number format.

### Number Format

The number format should be applied if attribute ss:xf:@applyNumberFormat has value 1 which means true. 

It will only format dates and time.

Following is a listing of number formats whose formatCode value is implied rather than explicitly saved in
the file. In this case a numFmtId value is written on the xf record, but no corresponding numFmt element is
written. Some of these Ids are interpreted differently, depending on the UI language of the implementing
application. This full list ends in the id 163.

| ID | formatCode                 |
|----|:--------------------------:|
| 0  | General                    | 
| 1  | 0                          |
| 2  | .00                        |
| 3  | ,##0                       |
| 4  | ,##0.00                    |
| 9  | %                          |
| 10 | 0.00%                      |
| 11 | 0.00E+00                   |
| 12 | # ?/?                      |
| 13 | # ??/??                    |
| 14 | mm-dd-yy                   |
| 15 | d-mmm-yy                   |
| 16 | d-mmm                      |
| 17 | mmm-yy                     |
| 18 | h:mm AM/PM                 |
| 19 | h:mm:ss AM/PM              |
| 20 | h:mm                       |
| 21 | h:mm:ss                    |
| 22 | m/d/yy h:mm                |
| 37 | #,##0 ;(#,##0)             |
| 38 | #,##0 ;[Red](#,##0)        |
| 39 | #,##0.00;(#,##0.00)        |
| 40 | #,##0.00;[Red](#,##0.00)   |
| 45 | mm:ss                      |
| 46 | [h]:mm:ss                  |
| 47 | mmss.0                     |
| 48 | ##0.0E+0                   |
| 49 | @                          |


#### Date and Time 
If it is date, the cell value is a serial date "http://www.ericwhite.com/blog/dates-in-spreadsheetml/".
And the base date is 31st December 1899 at 00:00:00.0000

It will only be applied for values without rich text.

Built in format implemented list
- 14 mm-dd-yy (show as dd-mm-yyyy) 

Custom format implemented List
- dd/mm/yyyy
- m/d/yyyy
- yyyy/mm/dd

Note: Please note that in order to calculate dates correctly, the year 1900 must be considered a leap-year hence 
February 29th is considered a valid date – even though the year 1900 is not a leap year.  This bug originated from 
Lotus 123, and was purposely implemented in Excel for backward compatibility.
