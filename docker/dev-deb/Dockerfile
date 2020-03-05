FROM debian:buster-slim

RUN set -ex \
    && echo "==> Updating repos ..." \
    && apt-get -y update \
    && echo "==> Installing packages ..." \
    && apt-get -y --no-install-recommends install \
        bzip2 \
        ca-certificates \
        curl \
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

CMD ["bash"]
