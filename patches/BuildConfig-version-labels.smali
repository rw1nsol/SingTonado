
.method public static getMainTabVersion()Ljava/lang/String;
    .locals 2
    const-string v0, "v"
    sget-object v1, Lsing/tona/BuildConfig;->VERSION_NAME:Ljava/lang/String;
    invoke-virtual {v0, v1}, Ljava/lang/String;->concat(Ljava/lang/String;)Ljava/lang/String;
    move-result-object v0
    return-object v0
.end method

.method public static getAppVersionLabel()Ljava/lang/String;
    .locals 2
    const-string v0, "SingTonado "
    sget-object v1, Lsing/tona/BuildConfig;->VERSION_NAME:Ljava/lang/String;
    invoke-virtual {v0, v1}, Ljava/lang/String;->concat(Ljava/lang/String;)Ljava/lang/String;
    move-result-object v0
    return-object v0
.end method

.method public static getAboutVersionLabel()Ljava/lang/String;
    .locals 2
    new-instance v0, Ljava/lang/StringBuilder;
    invoke-static {}, Lsing/tona/BuildConfig;->getAppVersionLabel()Ljava/lang/String;
    move-result-object v1
    invoke-direct {v0, v1}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V
    const-string v1, " \u00b7 build "
    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;
    sget v1, Lsing/tona/BuildConfig;->VERSION_CODE:I
    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;
    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;
    move-result-object v0
    return-object v0
.end method

.method public static getUpdatedMessage()Ljava/lang/String;
    .locals 2
    new-instance v0, Ljava/lang/StringBuilder;
    const-string v1, "Version "
    invoke-direct {v0, v1}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V
    sget-object v1, Lsing/tona/BuildConfig;->VERSION_NAME:Ljava/lang/String;
    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;
    const-string v1, ". Tap to open."
    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;
    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;
    move-result-object v0
    return-object v0
.end method
