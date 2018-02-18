package com.test.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@ToString
public class Price {
    Long id; // идентификатор в БД
    String product_code; // код товара
    int number; // номер цены
    int depart; // номер отдела
    Date begin; // начало действия
    Date end; // конец действия
    long value; // значение цены в копейках


    public Price copy() {
        return new Price(null, product_code, number, depart, begin, end, value);
    }
}
