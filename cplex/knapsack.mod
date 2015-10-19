/*********************************************
 * OPL 12.4 Model
 * Author: kbarreau
 * Creation Date: 13 oct. 2015 at 10:51:19
 *********************************************/

int Capacity = ...;

tuple item {
  int weight;
  int profit;
}

{item} Items = ...;

dvar int+ x[Items] in 0..1;

maximize sum(i in Items) x[i] * i.profit;

subject to {
  sum(i in Items)(i.weight * x[i]) <= Capacity;
}