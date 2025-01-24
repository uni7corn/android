package org.cryptomator.domain.usecases.vault;

import com.google.common.base.Optional;

import org.cryptomator.domain.Cloud;
import org.cryptomator.domain.UnverifiedVaultConfig;
import org.cryptomator.domain.exception.BackendException;
import org.cryptomator.domain.repository.CloudRepository;
import org.cryptomator.domain.usecases.cloud.Flag;
import org.cryptomator.generator.Parameter;
import org.cryptomator.generator.UseCase;

@UseCase
class UnlockVaultUsingMasterkey {

	private final CloudRepository cloudRepository;
	private final VaultOrUnlockToken vaultOrUnlockToken;
	private final String password;
	private Optional<UnverifiedVaultConfig> unverifiedVaultConfig;
	private volatile boolean cancelled;
	private final Flag cancelledFlag = new Flag() {
		@Override
		public boolean get() {
			return cancelled;
		}
	};

	public UnlockVaultUsingMasterkey(CloudRepository cloudRepository, @Parameter VaultOrUnlockToken vaultOrUnlockToken, @Parameter Optional<UnverifiedVaultConfig> unverifiedVaultConfig, @Parameter String password) {
		this.cloudRepository = cloudRepository;
		this.vaultOrUnlockToken = vaultOrUnlockToken;
		this.unverifiedVaultConfig = unverifiedVaultConfig;
		this.password = password;
	}

	public void onCancel() {
		cancelled = true;
	}

	public Cloud execute() throws BackendException {
		if (vaultOrUnlockToken.getVault().isPresent()) {
			return cloudRepository.unlock(vaultOrUnlockToken.getVault().get(), unverifiedVaultConfig, password, cancelledFlag);
		} else {
			return cloudRepository.unlock(vaultOrUnlockToken.getUnlockToken().get(), unverifiedVaultConfig, password, cancelledFlag);
		}
	}

}
