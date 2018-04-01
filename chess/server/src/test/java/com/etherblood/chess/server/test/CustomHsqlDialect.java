package com.etherblood.chess.server.test;

import java.util.UUID;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.UUIDTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

public class CustomHsqlDialect extends HSQLDialect {

	@Override
	public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
		super.contributeTypes(typeContributions, serviceRegistry);
		typeContributions.contributeType(new UUIDStringCustomType());
	}

	private class UUIDStringCustomType extends AbstractSingleColumnStandardBasicType<UUID> {

		private static final long serialVersionUID = 1L;

		public UUIDStringCustomType() {
			super(VarcharTypeDescriptor.INSTANCE, UUIDTypeDescriptor.INSTANCE);
		}

		@Override
		public String getName() {
			return "pg-uuid";
		}

	}
}