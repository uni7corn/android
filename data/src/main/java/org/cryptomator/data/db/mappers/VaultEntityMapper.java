package org.cryptomator.data.db.mappers;

import org.cryptomator.data.db.entities.VaultEntity;
import org.cryptomator.domain.Cloud;
import org.cryptomator.domain.CloudType;
import org.cryptomator.domain.Vault;
import org.cryptomator.domain.exception.BackendException;
import org.cryptomator.util.crypto.CryptoMode;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.cryptomator.domain.Vault.aVault;

@Singleton
public class VaultEntityMapper extends EntityMapper<VaultEntity, Vault> {

	private final CloudEntityMapper cloudEntityMapper;

	@Inject
	public VaultEntityMapper(CloudEntityMapper cloudEntityMapper) {
		this.cloudEntityMapper = cloudEntityMapper;
	}

	@Override
	public Vault fromEntity(VaultEntity entity) throws BackendException {
		return aVault() //
				.withId(entity.getId()) //
				.withName(entity.getFolderName()) //
				.withPath(entity.getFolderPath()) //
				.withCloud(cloudFrom(entity)) //
				.withCloudType(CloudType.valueOf(entity.getCloudType())) //
				.withSavedPassword(entity.getPassword(), cryptoModeFrom(entity)) //
				.withPosition(entity.getPosition()) //
				.withFormat(entity.getFormat()) //
				.withShorteningThreshold(entity.getShorteningThreshold()) //
				.build();
	}

	private Cloud cloudFrom(VaultEntity entity) {
		if (entity.getFolderCloud() == null) {
			return null;
		}
		return cloudEntityMapper.fromEntity(entity.getFolderCloud());
	}

	private CryptoMode cryptoModeFrom(VaultEntity entity) {
		return entity.getPasswordCryptoMode() != null ? CryptoMode.valueOf(entity.getPasswordCryptoMode()) : null;
	}

	@Override
	public VaultEntity toEntity(Vault domainObject) {
		VaultEntity entity = new VaultEntity();
		entity.setId(domainObject.getId());
		entity.setFolderPath(domainObject.getPath());
		entity.setFolderName(domainObject.getName());
		if (domainObject.getCloud() != null) {
			entity.setFolderCloud(cloudEntityMapper.toEntity(domainObject.getCloud()));
		}
		entity.setCloudType(domainObject.getCloudType().name());
		entity.setPassword(domainObject.getPassword());
		if (domainObject.getPasswordCryptoMode() != null) {
			entity.setPasswordCryptoMode(domainObject.getPasswordCryptoMode().name());
		}
		entity.setPosition(domainObject.getPosition());
		entity.setFormat(domainObject.getFormat());
		entity.setShorteningThreshold(domainObject.getShorteningThreshold());
		return entity;
	}
}
