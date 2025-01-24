package org.cryptomator.domain.repository;

import com.google.common.base.Optional;

import org.cryptomator.domain.exception.BackendException;
import org.cryptomator.domain.exception.update.GeneralUpdateErrorException;
import org.cryptomator.domain.usecases.UpdateCheck;

import java.io.File;

public interface UpdateCheckRepository {

	Optional<UpdateCheck> getUpdateCheck(String version) throws BackendException;

	String getLicense();

	void setLicense(String license);

	void update(File file) throws GeneralUpdateErrorException;
}
