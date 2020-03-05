FROM debian:buster-slim

RUN set -ex \
    && echo "==> Updating repos ..." \
    && apt-get -y update \
    && echo "==> Installing packages ..." \
    && apt-get -y --no-install-recommends install \
        bzip2 \
        ca-certificates \
        curl \
        git \
        gnupg \
        jq \
        less \
        nano \
        unzip \
        wget \
    && echo "==> Clean up ..." \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Default to UTF-8 file.encoding
ENV LANG C.UTF-8

COPY rootfs /

# https://github.com/corretto/corretto-11/releases
# deb package checksum, download will be validated using it
ENV JDK_MD5=501e257d5389e26a024c01b47b4a8d80
ENV JDK_PACKAGE_VERSION=11.0.6.10
ENV JDK_PACKAGE_NAME=java-11-amazon-corretto-jdk_${JDK_PACKAGE_VERSION}-1_amd64.deb
ENV JDK_URL=https://corretto.aws/downloads/resources/${JDK_PACKAGE_VERSION}.1/${JDK_PACKAGE_NAME}

RUN set -ex \
    # https://docs.aws.amazon.com/corretto/latest/corretto-8-ug/generic-linux-install.html
    && echo "==> Install JDK prerequisites ..." \
    && install_packages \
    java-common \
    fontconfig \
    && echo "==> Downloading Amazon Corretto 11 ..." \
    && wget -q ${JDK_URL} -P /tmp \
    && echo "==> Listing downloaded stuff ..." \
    && echo "${JDK_MD5} /tmp/${JDK_PACKAGE_NAME}" | md5sum -c - \
    && echo "==> Installing Amazon Corretto 11 ..." \
    # deal with slim variants not having man page directories (which causes "update-alternatives" to fail)
    && [ -d /usr/share/man/man1 ] || mkdir -p /usr/share/man/man1 \
    && dpkg --install /tmp/${JDK_PACKAGE_NAME} \
    && echo "==> Cleaning up ..." \
    && rm -f /tmp/${JDK_PACKAGE_NAME}

ENV JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto

ENV GRADLE_VERSION=6.2.2

RUN set -ex \
    && echo "===> Installing Gradle ..." \
    && wget -q "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" -O /tmp/gradle-bin.zip \
    && wget -q "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip.sha256" -O /tmp/gradle-bin.zip.sha256 \
    && cd /tmp/ \
    && echo "$(cat gradle-bin.zip.sha256) gradle-bin.zip" | sha256sum -c - \
    && mkdir /opt/gradle \
    && unzip -d /opt/gradle /tmp/gradle-bin.zip \
    && rm -f /tmp/gradle-bin.* \

ENV PATH=${PATH}:/opt/gradle/gradle-${GRADLE_VERSION}/bin

RUN set -ex \
    && echo "==> Install gcc ..." \
    && install_packages \
    gcc \
    g++

CMD ["bash"]
