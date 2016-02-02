package com.plexobject.dp.marshal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;

public class MarshallerFactoryTest {
    static class User {
        private String username;
        private String name;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result
                    + ((username == null) ? 0 : username.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            User other = (User) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (username == null) {
                if (other.username != null)
                    return false;
            } else if (!username.equals(other.username))
                return false;
            return true;
        }

    }

    static class UserMarshaller implements DataRowSetMarshaller<User> {
        private MetaField name = MetaFieldFactory.createText("name");
        private MetaField username = MetaFieldFactory.create("username",
                MetaFieldType.SCALAR_TEXT, true);

        @Override
        public DataRowSet marshal(User user) {
            DataRowSet rowset = new DataRowSet(getMetadata());
            rowset.addValueAtRow(name, user.getName(), 0);
            rowset.addValueAtRow(username, user.getUsername(), 0);
            return rowset;
        }

        @Override
        public User unmarshal(DataRowSet from) {
            User user = new User();
            user.setName(from.getValueAsText(name, 0));
            user.setUsername(from.getValueAsText(username, 0));
            return user;
        }

        @Override
        public Metadata getMetadata() {
            return Metadata.from(name, username);
        }

    }

    @Test
    public void testMarshalUnmarshal() {
        User user = new User();
        user.setName("jake");
        user.setUsername("jake1000");
        MarshallerFactory.getInstance().register(User.class, DataRowSet.class,
                new UserMarshaller());
        DataRowSet row = MarshallerFactory.getInstance()
                .getMarshaller(User.class, DataRowSet.class).marshal(user);
        User unmarshalledUser = MarshallerFactory.getInstance()
                .getMarshaller(User.class, DataRowSet.class).unmarshal(row);
        assertEquals(user, unmarshalledUser);
    }

}
