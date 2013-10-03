/*
 * Copyright (C) 2009 Pal Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice.recipient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import no.sws.client.SwsMissingRequiredElementInResponseException;
import no.sws.client.SwsParsingServerResponseException;
import no.sws.client.SwsRequiredInvoiceValueException;
import no.sws.recipient.RecipientCategory;
import no.sws.recipient.RecipientCategoryFactory;
import no.sws.util.XmlUtils;

/**
 * @author Pal Orby, SendRegning AS
 */
public class RecipientHelper {

	/**
	 * Helper method to generate a collection of XML elements of the given Recipient values.
	 * 
	 * @param recipient The Recipient to generate XML elements of.
	 * @return A collection of XML elements generated by the Recipient values.
	 * @throws SwsRequiredInvoiceValueException If a required value isn't set on the <code>Recipient</code>
	 * @throws NullPointerException If the given recipient is null
	 */
	public static List<Element> getRecipientValuesAsXmlElements(final Recipient recipient) throws SwsRequiredInvoiceValueException {

		if(recipient == null) {
			throw new NullPointerException("The given Recipient can't be null");
		}

		final LinkedList<Element> result = new LinkedList<Element>();

		final String name = recipient.getName();
		final String zip = recipient.getZip();
		final String city = recipient.getCity();

		// name is required
		if(name == null || name.trim().length() == 0) {
			throw new SwsRequiredInvoiceValueException("name");
		}
		else {
			result.add(new Element("name").setText(name));
		}
		// zip is required
		if(zip == null || zip.trim().length() == 0) {
			throw new SwsRequiredInvoiceValueException("zip");
		}
		else {
			result.add(new Element("zip").setText(zip));
		}
		// city is required
		if(city == null || city.trim().length() == 0) {
			throw new SwsRequiredInvoiceValueException("city");
		}
		else {
			result.add(new Element("city").setText(city));
		}

		return result;
	}

	public static List<Element> getOptionalRecipientValuesAsXmlElements(final Recipient recipient) {

		if(recipient == null) {
			throw new NullPointerException("Parameter recipient can't be null");
		}

		final LinkedList<Element> result = new LinkedList<Element>();

		final String recipientNo = recipient.getRecipientNo();
        final String recipientOrgNo = recipient.getOrgNo();
		final String address1 = recipient.getAddress1();
		final String address2 = recipient.getAddress2();
		final String country = recipient.getCountry();
        final String email = recipient.getEmail();

        // recipientNo is optional
		if(recipientNo != null && recipientNo.trim().length() > 0) {
			result.add(new Element("recipientNo").setText(recipientNo));
		}

        // recipientOrgNo is optional
        if(recipientOrgNo != null && recipientOrgNo.trim().length() > 0) {
            result.add(new Element("recipientOrgNo").setText(recipientOrgNo));
        }

		// address1 is optional
		if(address1 != null && address1.trim().length() > 0) {
			result.add(new Element("address1").setText(address1));
		}
		// address2 is optional
		if(address2 != null && address2.trim().length() > 0) {
			result.add(new Element("address2").setText(address2));
		}

		// country is optional, the server will use NORGE if omitted
		if(country != null && country.trim().length() > 0) {
			result.add(new Element("country").setText(country));
		}

        // email is optional
        if (email != null && email.trim().length() > 0) {
            result.add(new Element("email").setText(email));
        }

		return result;
	}
	
	public static Recipient mapRecipientResponseToRecipient(String response) throws JDOMException, IOException, SwsParsingServerResponseException, SwsMissingRequiredElementInResponseException {
		
		if(response == null || response.trim().length() == 0) {
			throw new IllegalArgumentException("Param response can't be null or an zero length String");
		}
		
		// convert response to XML (JDom)
		Document xml = XmlUtils.string2Xml(response);
		
		Element rootElement = xml.getRootElement();
		
		if(rootElement != null && rootElement.getName().equals("recipients") && rootElement.getChildren("recipient").size() > 0) {

			// get the first, and only, recipient element 
			Element recipientElement = rootElement.getChild("recipient");
					
			return recipientElem2Recipient(recipientElement);
		}
		else {
			throw new SwsParsingServerResponseException(response);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Recipient> mapRecipientResponseToRecipientList(String response) throws SwsParsingServerResponseException, SwsMissingRequiredElementInResponseException, JDOMException, IOException {

		if(response == null || response.trim().length() == 0) {
			throw new IllegalArgumentException("Param response can't be null or an zero length String");
		}
		
		// convert response to XML (JDom)
		Document xml = XmlUtils.string2Xml(response);
		
		Element rootElement = xml.getRootElement();
		
		if(rootElement != null && rootElement.getName().equals("recipients") && rootElement.getChildren("recipient").size() > 0) {

			// get all child recipient elements
			List<Element> recipientElements = rootElement.getChildren("recipient");
			
			List<Recipient> result = new LinkedList<Recipient>();
				
			for(Element currentRecipientElement : recipientElements) {
				result.add(recipientElem2Recipient(currentRecipientElement));
			}
			
			return result;
		}
		else {
			throw new SwsParsingServerResponseException(response);
		}
	}

	@SuppressWarnings("unchecked")
	public static List<RecipientCategory> mapRecipientCategoriesResponseToRecipientCategoryList(String response) throws JDOMException, IOException, SwsParsingServerResponseException, SwsMissingRequiredElementInResponseException {

		if(response == null || response.trim().length() == 0) {
			throw new IllegalArgumentException("Param response can't be null or an zero length String");
		}
		
		// convert response to XML (JDom)
		Document xml = XmlUtils.string2Xml(response);
		
		Element rootElement = xml.getRootElement();
		
		if(rootElement != null && rootElement.getName().equals("categories") && rootElement.getChildren("category").size() > 0) {
			
			// get all child category elements
			List<Element> categoryElements = rootElement.getChildren("category");
			
			List<RecipientCategory> result = new LinkedList<RecipientCategory>();
			
			for(Element currentCategoryElement : categoryElements) {
				
				String categoryName = getChildElementValue(currentCategoryElement, "name", Boolean.TRUE);
				Integer recipientCount = Integer.parseInt(getChildElementValue(currentCategoryElement, "recipientCount", Boolean.TRUE));
				
				result.add(RecipientCategoryFactory.getInstance(categoryName, recipientCount));
			}
			
			return result;
		}
		else {
			throw new SwsParsingServerResponseException(response);
		}
	}
	
	private static Recipient recipientElem2Recipient(Element recipientElement) throws SwsMissingRequiredElementInResponseException {
		
		if(recipientElement == null || recipientElement.getChildren().size() == 0) {
			throw new IllegalArgumentException("Param recipientElement can't be null or an Element without any child elements");
		}
		
		Recipient result = RecipientFactory.getInstance();
		
		// required values
		result.setName(getChildElementValue(recipientElement, "name", Boolean.TRUE));
		result.setZip(getChildElementValue(recipientElement, "zip", Boolean.TRUE));
		result.setCity(getChildElementValue(recipientElement, "city", Boolean.TRUE));
		
		// optional values
		Element optionalElement = recipientElement.getChild("optional");
		
		if(optionalElement != null && optionalElement.getChildren().size() > 0) {
			
			result.setAddress1(getChildElementValue(optionalElement, "address1", Boolean.FALSE));
			result.setAddress2(getChildElementValue(optionalElement, "address2", Boolean.FALSE));
			result.setCountry(getChildElementValue(optionalElement, "country", Boolean.FALSE));
			result.setRecipientNo(getChildElementValue(optionalElement, "recipientNo", Boolean.FALSE));
            result.setOrgNo(getChildElementValue(optionalElement, "recipientOrgNo", Boolean.FALSE));
			result.setPhone(getChildElementValue(optionalElement, "phone", Boolean.FALSE));
			result.setMobile(getChildElementValue(optionalElement, "mobile", Boolean.FALSE));
			result.setFax(getChildElementValue(optionalElement, "fax", Boolean.FALSE));
			result.setEmail(getChildElementValue(optionalElement, "email", Boolean.FALSE));
			result.setWeb(getChildElementValue(optionalElement, "web", Boolean.FALSE));
			result.setCategory(getChildElementValue(optionalElement, "category", Boolean.FALSE));
			result.setComment(getChildElementValue(optionalElement, "comment", Boolean.FALSE));
			result.setCreditDays(getChildElementValue(optionalElement, "creditDays", Boolean.FALSE));
			result.setPreferredShipment(getChildElementValue(optionalElement, "preferredShipment", Boolean.FALSE));
			result.setAttachPdf(getChildElementValue(optionalElement, "attachPdf", Boolean.FALSE));

            //add multiple categories
            List<String> categories = new ArrayList<String>();

            Element categoriesElement = optionalElement.getChild("categories");

            if(categoriesElement != null &&  categoriesElement.getChildren("category").size() > 0) {
                List<Element> catElements = categoriesElement.getChildren();

                for(Element e : catElements){
                    categories.add(e.getValue());
                }
		    }
            result.setCategories(categories);
        }
		
		return result;
	}
	
	private static String getChildElementValue(Element element, String name, Boolean required) throws SwsMissingRequiredElementInResponseException {
		
		Element child = element.getChild(name);
	
		if(child == null) {
			
			if(required) {
				throw new SwsMissingRequiredElementInResponseException("name", "recipient");
			}
			else {
				return null;
			}
		}
		else {
			String result = child.getTextNormalize();
			
			if(result.trim().length() == 0) {
				return null;
			}
			else {
				return result;
			}
		}
	}

	/**
	 * Checks that the following required values are set.
	 * <ul>
	 * <li>name
	 * <li>zip
	 * <li>city
	 * </ul>
	 * 
	 * @param recipient to validate
	 * @return True or false
	 */
	public static boolean validate(final Recipient recipient) {

		if(recipient == null) {
			return false;
		}
		else {

			if(recipient.getName() == null || recipient.getName().trim().length() == 0) {
				return false;
			}
			else if(recipient.getZip() == null || recipient.getZip().trim().length() == 0) {
				return false;
			}
			else if(recipient.getCity() == null || recipient.getCity().trim().length() == 0) {
				return false;
			}
			else {
				return true;
			}
		}
	}
}
