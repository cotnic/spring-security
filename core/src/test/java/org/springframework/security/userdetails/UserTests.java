/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.userdetails;

import java.util.List;

import junit.framework.TestCase;


import org.springframework.security.core.AuthorityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.GrantedAuthorityImpl;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.UserDetails;


/**
 * Tests {@link User}.
 *
 * @author Ben Alex
 * @version $Id$
 */
public class UserTests extends TestCase {
    private static final List<GrantedAuthority> ROLE_12 = AuthorityUtils.createAuthorityList("ROLE_ONE","ROLE_TWO");
    //~ Methods ========================================================================================================

    public void testEquals() {
        User user1 = new User("rod", "koala", true, true, true, true,ROLE_12);

        assertFalse(user1.equals(null));
        assertFalse(user1.equals("A STRING"));
        assertTrue(user1.equals(user1));
        assertTrue(user1.equals(new User("rod", "koala", true, true, true, true,ROLE_12)));
        // Equal as the new User will internally sort the GrantedAuthorities in the correct order, before running equals()
        assertTrue(user1.equals(new User("rod", "koala", true, true, true, true,
                        AuthorityUtils.createAuthorityList("ROLE_TWO","ROLE_ONE"))));
        assertFalse(user1.equals(new User("DIFFERENT_USERNAME", "koala", true, true, true, true, ROLE_12)));
        assertFalse(user1.equals(new User("rod", "DIFFERENT_PASSWORD", true, true, true, true, ROLE_12)));
        assertFalse(user1.equals(new User("rod", "koala", false, true, true, true, ROLE_12)));
        assertFalse(user1.equals(new User("rod", "koala", true, false, true, true, ROLE_12)));
        assertFalse(user1.equals(new User("rod", "koala", true, true, false, true, ROLE_12)));
        assertFalse(user1.equals(new User("rod", "koala", true, true, true, false, ROLE_12)));
        assertFalse(user1.equals(new User("rod", "koala", true, true, true, true,
                AuthorityUtils.createAuthorityList("ROLE_ONE"))));
    }

    public void testNoArgConstructorDoesntExist() {
        Class<User> clazz = User.class;

        try {
            clazz.getDeclaredConstructor((Class[]) null);
            fail("Should have thrown NoSuchMethodException");
        } catch (NoSuchMethodException expected) {
        }
    }

    public void testNullValuesRejected() throws Exception {
        try {
            new User(null, "koala", true, true, true, true,ROLE_12);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new User("rod", null, true, true, true, true, ROLE_12);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            List<GrantedAuthority> auths = AuthorityUtils.createAuthorityList("ROLE_ONE");
            auths.add(null);
            new User("rod", "koala", true, true, true, true, auths);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testNullWithinGrantedAuthorityElementIsRejected() throws Exception {
        try {
            List<GrantedAuthority> auths = AuthorityUtils.createAuthorityList("ROLE_ONE");
            auths.add(null);
            auths.add(new GrantedAuthorityImpl("ROLE_THREE"));
            new User(null, "koala", true, true, true, true, auths);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testUserGettersSetter() throws Exception {
        UserDetails user = new User("rod", "koala", true, true, true, true,
                AuthorityUtils.createAuthorityList("ROLE_TWO","ROLE_ONE"));
        assertEquals("rod", user.getUsername());
        assertEquals("koala", user.getPassword());
        assertTrue(user.isEnabled());
        assertEquals(new GrantedAuthorityImpl("ROLE_ONE"), user.getAuthorities().get(0));
        assertEquals(new GrantedAuthorityImpl("ROLE_TWO"), user.getAuthorities().get(1));
        assertTrue(user.toString().indexOf("rod") != -1);
    }

    public void testUserIsEnabled() throws Exception {
        UserDetails user = new User("rod", "koala", false, true, true, true, ROLE_12);
        assertTrue(!user.isEnabled());
    }
}
