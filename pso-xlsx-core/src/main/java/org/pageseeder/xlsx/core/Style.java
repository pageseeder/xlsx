/*
 * Copyright 2021 Allette Systems (Australia)
 * http://www.allette.com.au
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pageseeder.xlsx.core;

import java.util.*;

/**
 *
 */
public class Style {

  /**
   * Elements:
   *   numFmts/numFmts
   * Attributes:
   *   numFmtId = Map key
   *   formatCode = Map value
   */
  private final Map<Integer, String> numberFormats = new TreeMap<>();

  //cellXfs cellFormats
  /**
   * Elements cellXfs/xf
   */
  private final List<CellFormat> cellFormats = new ArrayList<>();

  /**
   * The number formats ids from 0 to 163 are already defined in ECMA 376.
   */
  private final static String [] DEFAULT_NUMBER_FORMATS = {
    "",
    "0",
    "0.00",
    "#,##0",
    "#,##0.00",
    "",//5
    "",
    "",
    "",
    "0%",
    "0.00%",//10
    "0.00E+00",
    "# ?/?",
    "# ??/??",
    "mm-dd-yy",
    "d-mmm-yy",//15
    "d-mmm",
    "mmm-yy",
    "h:mm AM/PM",
    "h:mm:ss AM/PM",
    "h:mm",//20
    "h:mm:ss",
    "m/d/yy h:mm",
  };


  public void addNumberFormat(Integer numberFormatId, String formatCode) {
    this.numberFormats.put(numberFormatId, formatCode);
  }

  public void addCellFormat(CellFormat cellFormat){
    this.cellFormats.add(cellFormat);
  }

  public CellFormat getCellFormat (int index) {
    return this.cellFormats.get(index);
  }

  public String getNumberFormatCode(int numberFormatID) {
    String formatCode = "";
    if (numberFormatID < 164) {
      if (numberFormatID < DEFAULT_NUMBER_FORMATS.length) {
        formatCode = DEFAULT_NUMBER_FORMATS[numberFormatID];
      }
    } else {
      if (this.numberFormats.containsKey(numberFormatID)) {
        formatCode = this.numberFormats.get(numberFormatID);
      }
    }
    return formatCode;
  }

  /**
   * "mm-dd-yy",
   * "d-mmm-yy",//15
   * "d-mmm",
   * "mmm-yy",
   * "m/d/yy h:mm",
   * "m/d/yyyy;@",
   * "yyyy/mm/dd",
   * "dd/mm/yyyy;@",
   * "[$-409]d\-mmm\-yy",
   *
   * @param numberFormatCode
   * @return
   */
  public static boolean isDateFormat (String numberFormatCode) {
    boolean is = false;
    if (numberFormatCode != null) {
      is = numberFormatCode.matches(".*[dy]+.*");
    }
    return is;
  }

  /*
   *
   * "m/d/yy h:mm",
   * "h:mm AM/PM",
   * "h:mm:ss AM/PM",
   * "h:mm"
   * "h:mm:ss",
   */
  public static boolean isTimeFormat (String numberFormatCode) {
    boolean is = false;
    if (numberFormatCode != null) {
      is = numberFormatCode.matches(".*[hs]+.*");
    }
    return is;
  }

  /*

  "mm-dd-yy",
        "d-mmm-yy",//15
        "d-mmm",
        "mmm-yy",
        "m/d/yy h:mm",
            m/d/yyyy;@
        yyyy/mm/dd
        dd/mm/yyyy;@
        [$-409]d\-mmm\-yy
        "h:mm AM/PM",
        "h:mm:ss AM/PM",
        "h:mm",//20
        "h:mm:ss",
   */

}
