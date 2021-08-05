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

/**
 *
 *
 * Represents the style.xml element cellXfs/xf
 */
public class CellFormat {

  /**
   * The position (zero-based) of xf within cellXfs.
   */
  private final int index;
  /**
   * attribute numFmtId
   */
  private final int numberFormatId;
  /**
   * attribute applyNumberFormat
   */
  private final boolean applyNumberFormat;

  public CellFormat(int index, int numberFormatId, boolean applyNumberFormat) {
    this.index = index;
    this.numberFormatId = numberFormatId;
    this.applyNumberFormat = applyNumberFormat;
  }

  public int getIndex() {
    return index;
  }

  public int getNumberFormatId() {
    return numberFormatId;
  }

  public boolean isApplyNumberFormat() {
    return applyNumberFormat;
  }


}
