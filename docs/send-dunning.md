Send dunning
============

In order to send out a dunning, the original invoice you want to dun must be sent out via !SendRegning.no. The original invoice must also be in the state `dueDecide`.

You can also find more information about when you can add to dunning fee/interest [here](https://www.sendregning.no/hjelp/sende-purring-eller-inkassovarsel/)

### Delivery method

A dunning must be printed out and sent to the recipient by paper mail.

The reason for this is the wording in the Act relating to overdue payments, which says:

_Demand can be sent electronically if the debtor has expressly accepted the use of such communication._

The Norwegian Consumer Council says the following about this:

_The Norwegian Consumer Council is in doubt whether it is desirable that demands can be sent electronically, even if the debtor has expressly accepted this._

The lawyers/auditors we have had contact with regarding this, say quite clearly that if there is to be some weight to the demand, a dunning should be sent on paper.

#### Example of the smallest possible XML for sending out a dunning

```xml
<?xml version="1.0" encoding="UTF-8"?>
<dunnings>
  <dunning>
    <invoiceNo>100453</invoiceNo>
  </dunning>
</dunnings>
```

Your invoice with number 100453 will now be dunned with no dunning fee or interest. Since it isn't sent out a dunning for this invoice before, it will be 1st DUNNING and the dunning text will be "Vi kan dessverre ikke se å ha mottatt Deres betaling for denne regning." with 14 due days.

#### Example of sending out a dunning with dunning fee, interest, 21 due days and a custom message

```xml
<?xml version="1.0" encoding="UTF-8"?>
<dunnings>
  <dunning>
    <invoiceNo>100453</invoiceNo>
    <dunningFee>true</dunningFee>
    <interest>true</interest>
    <dunningDays>21</dunningDays>
    <dunningText>Kjære kunde, vennligst betal utestående beløp</dunningText>
  </dunning>
</dunnings>
```

### Description

| Element name | The element's parents | Element description | Data type | Standard value
|--------------|-----------------------|---------------------|-----------|---------------
| `<dunnings>` | `-` | *Dunnings*<br>Contains one or more dunning elements | - | -
| `<dunning>` | `<dunnings>` | *Dunning*<br>Contains a dunning. | - | -
| `<invoiceNo>` | `<dunning>` | *Invoice number*<br>Invoice number for the invoice to be dunned.  This is a required element, and the invoice must exist and be in the state `dueDecide`. | Integer, maximum 15 characters | -
| `<dunningFee>` | `<dunning>` | *Dunning fee*<br>Whether a dunning fee will be added to the dunning or not.<br><br>*NOTE:* This does *not* specify the size of the dunning fee, which is already established at 1/10 of a collection agency fee. As of today (10.03.09), it is at NOK 59.<br>If this element is omitted, a dunning fee is *not* added to this dunning.<br><br>*Prerequisites for adding a dunning fee*<br>*§1* The original invoice must contain information that a dunning fee can be added when dunning. See the element `<printDunningInfo>` in XML when sending an invoice.<br>*§2* It is only possible to add a dunning fee twice. I.e. if you add a dunning fee to 1st DUNNING and 2nd DUNNING, you are *not* allowed to add a dunning fee when sending 3rd DUNNING or a DEBT COLLECTION NOTICE.<br>*§3* It must be past at least 14 days overdue for the original invoice or last sent dunning before a dunning with a dunning fee can be sent.<br><br>_If one or more of these prerequisites are not met, and the value of  `<dunningFee>` is set to `true`, this value will be ignored. This is done so as not to stop the transfer when the solution is to send the dunning without a dunning fee._ | Boolean value | false
| `<interest>` | `<dunning>` | *Interest calculation*<br>Whether interest will be calculated and added to the dunning or not.<br><br>*NOTE:* This does *not* specify the interest rate, which is already established by the authorities. As of today (10.03.09), the interest rate is 10%.<br>If this element is omitted, interest will *not* be calculated for this dunning. Interest will be calculated from the due date of the original invoice to the due date of the dunning.<br><br>*Prerequisities for adding interest*<br>*§1* The original invoice must contain information that interest can be added when dunning.<br><br>See the element `<printDunningInfo>` in XML when sending an invoice.<br><br>_If this prerequisite is not met, and the value of  `<dunningFee>` is set to `true`, this value will be ignored.<br>This is done so as not to stop the transfer when the solution is to send the dunning without interest._ | Boolean value | false
| `<dunningType>` | `<dunning>` | *Dunning level*<br>If this element is omitted the invoice will follow this dunning course: `1. DUNNING`, `2nd DUNNING`, and `DEBT COLLECTION NOTICE`.<br><br>However, you can send out up to 9 dunnings + debt collection notice via SendRegning.no, but you have to follow the sequence. For example, you will not be allowed to send out `2nd DUNNING` before you have sent out `1st DUNNING`, etc. However, at any time, you can send out a `DEBT COLLECTION NOTICE` if the invoice is in the state `dueDecide`. But you can't send out any dunnings after you have sent out a `DEBT COLLECTION NOTICE`.<br><br>The following values can be set in the `<dunningType>` element:<br><br>`1Dunning` - 1st DUNNING<br>`2Dunning` - 2nd DUNNING<br>`3Dunning` - 3rd DUNNING<br>`4Dunning` - 4th DUNNING<br>`5Dunning` - 5th DUNNING<br>`6Dunning` - 6th DUNNING<br>`7Dunning` - 7th DUNNING<br>`8Dunning` - 8th DUNNING<br>`9Dunning` - 9th DUNNING<br>`deptCollectionNotice` - DEBT COLLECTION NOTICE | Text, maximum 20 characters | Next in series (1Dunning, 2Dunning, deptCollectionNotice)
| `<dunningText>` | `<dunning>` | *Dunning text*<br>Text field with space for a message to the recipient of the dunning.  If this element is omitted, and you send out a dunning *except for* `DEBT COLLECTION NOTICE`, the following standard text will be used:<br><br>`Vi kan dessverre ikke se å ha mottatt Deres betaling for denne regning.`.<br><br>If the element is omitted, and you send out a `DEBT COLLECTION NOTICE`, the following standard text will be used:<br><br>`Dersom vi ikke har mottatt Deres betaling innen forfall, overføres regningen til inkasso.`<br><br>*NOTE:* This text is printed instead of what you may have specified in the `<invoiceText>` element when sending the original invoice. | Text - maximum 105 characters | Standard text - see description
| `<dunningDays>` | `<dunning>` | *Dunning days*<br>Number of dunning days for this dunning.  If this element is omitted, 14 days is used as the number of dunning days for the dunning.<br><br>*NOTE:* It is legally required that the dunning must have a minimum of 14 days payment deadline. | Integer >= 14 | 14

### Respons

You will get the same response from SWS as if you had sent the dunning as a regular invoice.
