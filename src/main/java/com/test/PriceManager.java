package com.test;

import com.test.model.Price;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PriceManager {

    public List<Price> priceMerge(List<Price> oldPrices, List<Price> newPrices) {
        boolean isEnd = false;
        while (!isEnd) {
            isEnd = true;


            for (int i = 0; i < oldPrices.size(); i++) {
                Price oldPrice = oldPrices.get(i);
                if (oldPrice.getBegin().equals(oldPrice.getEnd())) {
                    oldPrices.remove(i);
                    isEnd = false;
                    break;//restart cycle
                }
                Iterator<Price> iter = newPrices.iterator();
                while (iter.hasNext()) {
                    Price newPrice = iter.next();
                    if (oldPrice.getProduct_code().equals(newPrice.getProduct_code()) && oldPrice.getNumber() == newPrice.getNumber() && oldPrice.getDepart() == newPrice.getDepart()) {

                        //case 1
                        if ((newPrice.getBegin().after(oldPrice.getBegin()) || newPrice.getBegin().equals(oldPrice.getBegin()))
                                && (newPrice.getEnd().before(oldPrice.getEnd()) || newPrice.getEnd().equals(oldPrice.getEnd()))) {
                            Date savedEnd = oldPrice.getEnd();
                            oldPrice.setEnd(newPrice.getBegin());
                            oldPrices.add(newPrice);
                            Price thirdPart = oldPrice.copy();
                            thirdPart.setBegin(newPrice.getEnd());
                            thirdPart.setEnd(savedEnd);
                            oldPrices.add(thirdPart);
                            iter.remove();
                        }

                        //case 2
                        if (newPrice.getBegin().after(oldPrice.getBegin()) && newPrice.getBegin().before(oldPrice.getEnd()) && newPrice.getEnd().after(oldPrice.getEnd())) {
                            if (newPrice.getValue() == oldPrice.getValue()) {
                                oldPrice.setEnd(newPrice.getEnd());
                                iter.remove();
                            } else {
                                Date savedEnd = oldPrice.getEnd();
                                oldPrice.setEnd(newPrice.getBegin());
                                Price secondPart = newPrice.copy();
                                secondPart.setEnd(savedEnd);
                                oldPrices.add(secondPart);
                                newPrice.setBegin(savedEnd);
                                isEnd = false;
                            }
                        } else {
                            //mirror case
                            if (newPrice.getBegin().before(oldPrice.getBegin()) && newPrice.getEnd().after(oldPrice.getBegin()) && newPrice.getEnd().before(oldPrice.getEnd())) {
                                if (newPrice.getValue() == oldPrice.getValue()) {
                                    oldPrice.setBegin(newPrice.getBegin());
                                    iter.remove();
                                } else {
                                    Date savedBeginOfOldPrice = oldPrice.getBegin();
                                    oldPrice.setBegin(newPrice.getEnd());
                                    Price part2 = newPrice.copy();
                                    part2.setBegin(savedBeginOfOldPrice);
                                    oldPrices.add(part2);
                                    newPrice.setEnd(savedBeginOfOldPrice);
                                    isEnd = false;
                                }
                            }
                        }
                    }
                }

            }
        }

        //union all
        Iterator<Price> priceIterator = oldPrices.iterator();
        while (priceIterator.hasNext()) {
            Price price1 = priceIterator.next();
            int index = 0;
            while (index < oldPrices.size()) {
                Price price2 = oldPrices.get(index);
                if (price1.getId() == price2.getId() && price1.getId() != null) {
                    index += 1;
                    continue;//skip
                }
                if (price1.getEnd().equals(price2.getBegin()) && price1.getDepart() == price2.getDepart() &&
                        price1.getValue() == price2.getValue() && price1.getNumber() == price2.getNumber()) {
                    price2.setBegin(price1.getBegin());
                    priceIterator.remove();
                    break;
                }
                index += 1;
            }
        }

        return oldPrices;

    }

}
