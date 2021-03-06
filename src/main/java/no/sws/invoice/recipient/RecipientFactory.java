/*
 * Copyright (C) 2009 Pal Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice.recipient;

/**
 * @author Pal Orby, SendRegning AS
 */
public class RecipientFactory {

	/**
	 * Shortcut, normally you would get an instance of <code>RecipientFactory</code>, but this gives you an
	 * <code>Recipient</code> directly.
	 * 
	 * @return An </code>Recipient</code>
	 */
	public static Recipient getInstance() {

		return new RecipientImpl();
	}
}
