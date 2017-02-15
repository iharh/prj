ChosenChangeEvent not triggered after Chosen.setSelectedItem(n)
----
This GWT project is aimed to help reproduce the [bug][]
in GWT-Chosen where a ChosenChangeEvent is not triggered.

The application configures a ChosenChangeHandler to open popup a window each time you select a list item.
A ChosenChangeEvent is not triggered when selecting a list item in the 3rd step below whereas it should.

Procedure to reproduce the bug 
---
1. Select a list item, _Java_ for instance. 
The expected window pops up.

2. Click the button labeled _chosenListBox.setSelectedItem(0)_. 
This calls `setSelectedItem(0)` on the ChosenListBoxi, selecting the first list item (ie. _LOL_).

3. Re-select the very same list item as before _Java_. 
This time the expected window *does not* pop up because no ChosenChangeEvent is triggered.

[bug]: https://github.com/jDramaix/gwtchosen/issues/29  "Bug"
