package org.cryptomator.data.db.entities;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

@Entity(indexes = {@Index(value = "folderPath,folderCloudId", unique = true)})
public class VaultEntity extends DatabaseEntity {

	@Id
	private Long id;

	private Long folderCloudId;

	@ToOne(joinProperty = "folderCloudId")
	private CloudEntity folderCloud;

	private String folderPath;

	private String folderName;

	@NotNull
	private String cloudType;

	private String password;

	private String passwordCryptoMode;

	private Integer position;

	private Integer format;

	private Integer shorteningThreshold;

	/**
	 * Used for active entity operations.
	 */
	@Generated(hash = 941685503)
	private transient VaultEntityDao myDao;
	/**
	 * Used to resolve relations
	 */
	@Generated(hash = 2040040024)
	private transient DaoSession daoSession;

	@Generated(hash = 229273163)
	private transient Long folderCloud__resolvedKey;

	@Generated(hash = 1663458645)
	public VaultEntity(Long id, Long folderCloudId, String folderPath, String folderName, @NotNull String cloudType, String password, String passwordCryptoMode, Integer position, Integer format,
			Integer shorteningThreshold) {
		this.id = id;
		this.folderCloudId = folderCloudId;
		this.folderPath = folderPath;
		this.folderName = folderName;
		this.cloudType = cloudType;
		this.password = password;
		this.passwordCryptoMode = passwordCryptoMode;
		this.position = position;
		this.format = format;
		this.shorteningThreshold = shorteningThreshold;
	}

	@Generated(hash = 691253864)
	public VaultEntity() {
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 1942392019)
	public void refresh() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.refresh(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 713229351)
	public void update() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.update(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 128553479)
	public void delete() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.delete(this);
	}

	/**
	 * To-one relationship, resolved on first access.
	 */
	@Generated(hash = 1508817413)
	public CloudEntity getFolderCloud() {
		Long __key = this.folderCloudId;
		if (folderCloud__resolvedKey == null || !folderCloud__resolvedKey.equals(__key)) {
			final DaoSession daoSession = this.daoSession;
			if (daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			CloudEntityDao targetDao = daoSession.getCloudEntityDao();
			CloudEntity folderCloudNew = targetDao.load(__key);
			synchronized (this) {
				folderCloud = folderCloudNew;
				folderCloud__resolvedKey = __key;
			}
		}
		return folderCloud;
	}

	/**
	 * called by internal mechanisms, do not call yourself.
	 */
	@Generated(hash = 1482096330)
	public void setFolderCloud(CloudEntity folderCloud) {
		synchronized (this) {
			this.folderCloud = folderCloud;
			folderCloudId = folderCloud == null ? null : folderCloud.getId();
			folderCloud__resolvedKey = folderCloudId;
		}
	}

	public String getFolderPath() {
		return this.folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFolderCloudId() {
		return this.folderCloudId;
	}

	public void setFolderCloudId(Long folderCloudId) {
		this.folderCloudId = folderCloudId;
	}

	public String getCloudType() {
		return this.cloudType;
	}

	public void setCloudType(String cloudType) {
		this.cloudType = cloudType;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getPosition() {
		return this.position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getFormat() {
		return this.format;
	}

	public void setFormat(Integer format) {
		this.format = format;
	}

	public Integer getShorteningThreshold() {
		return this.shorteningThreshold;
	}

	public void setShorteningThreshold(Integer shorteningThreshold) {
		this.shorteningThreshold = shorteningThreshold;
	}

	public String getPasswordCryptoMode() {
		return this.passwordCryptoMode;
	}

	public void setPasswordCryptoMode(String passwordCryptoMode) {
		this.passwordCryptoMode = passwordCryptoMode;
	}

	/** called by internal mechanisms, do not call yourself. */
	@Generated(hash = 674742652)
	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		myDao = daoSession != null ? daoSession.getVaultEntityDao() : null;
	}

}
