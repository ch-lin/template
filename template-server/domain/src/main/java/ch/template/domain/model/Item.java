/*=============================================================================
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Che-Hung Lin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *===========================================================================*/

package ch.template.domain.model;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

import static ch.template.domain.model.Item.*;

@Table(name = TABLE_NAME, indexes = {@Index(name = ITEM_ID_IDX, columnList = ITEM_ID)},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@Entity
public class Item {

    public static final String TABLE_NAME = "Item";
    public static final String ITEM_ID = "ItemID";
    public static final String ITEM_ID_IDX = "item_id_index";

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ITEM_ID)
    private long itemID;

    @Getter
    @Setter
    @NotNull
    private String name = null;

    @Getter
    @Setter
    private Long num;

    public Item() {
    }

    public Item(String name, Long num) {
        this.name = name;
        this.num = num;
    }

}