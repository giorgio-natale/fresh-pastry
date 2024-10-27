package it.gionatale.fp.orderservice.persistenceconfig;


import org.hibernate.HibernateException;
import org.hibernate.annotations.CompositeTypeRegistration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.spi.ValueAccess;
import org.hibernate.usertype.CompositeUserType;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

@CompositeTypeRegistration(userType = MonetaryAmountUserType.class, embeddableClass = MonetaryAmountUserType.MonetaryAmountEmbeddable.class)
public class MonetaryAmountUserType implements CompositeUserType<MonetaryAmount> {

    @Override
    public Object getPropertyValue(MonetaryAmount monetaryAmount, int property) {
        return switch (property) {
            case 0 -> monetaryAmount.getCurrency().getCurrencyCode();
            case 1 -> monetaryAmount.getNumber().numberValue(BigDecimal.class);
            default -> throw new HibernateException("Illegal property orderItemIndex: " + property);
        };
    }

    @Override
    public MonetaryAmount instantiate(ValueAccess valueAccess, SessionFactoryImplementor sessionFactory) {
        final String currency = valueAccess.getValue(0, String.class);
        final BigDecimal value = valueAccess.getValue(1, BigDecimal. class);

        if ( value == null || currency == null ) {
            return null;
        }

        return Money.of(value, currency);
    }

    @Override
    public Class<MonetaryAmountEmbeddable> embeddable() {
        return MonetaryAmountEmbeddable.class;
    }

    @Override
    public Class<MonetaryAmount> returnedClass() {
        return MonetaryAmount.class;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public MonetaryAmount deepCopy(MonetaryAmount value) {
        return value; // MonetaryAmount is immutable
    }

    @Override
    public boolean equals(MonetaryAmount x, MonetaryAmount y) {
        if ( x == y ) {
            return true;
        }
        if ( x == null || y == null ) {
            return false;
        }
        return x.equals( y );
    }

    @Override
    public Serializable disassemble(MonetaryAmount value) {
        return (Serializable) value;
    }

    @Override
    public MonetaryAmount assemble(Serializable cached, Object owner) {
        return (MonetaryAmount) cached;
    }

    @Override
    public MonetaryAmount replace(MonetaryAmount original, MonetaryAmount target, Object owner) {
        return original;
    }

    @Override
    public int hashCode(MonetaryAmount x) throws HibernateException {
        return x.hashCode();
    }

    // the embeddable class which acts as a source of metadata
    public static class MonetaryAmountEmbeddable {
        private String currency;
        private BigDecimal value;
    }
}