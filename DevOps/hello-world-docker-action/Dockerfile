# Container image that runs your code
FROM ubuntu:20.04
MAINTAINER vishwambhar.kapare@paroscale.com
RUN apt-get update \
    && apt-get -y install python3-pip \ 
    && apt-get -y install curl \
    && apt-get -y install zip unzip \
    && apt-get install -y jq \
    && pip3 install ansible

# Copies your code file from your action repository to the filesystem path `/` of the container
COPY entrypoint.sh /entrypoint.sh
WORKDIR /app
COPY random_no_gen.py /app
COPY down_rel.sh /app
RUN ls -l
RUN chmod +x down_rel.sh \
    && ./down_rel.sh  0dd182e1864626219b032d495254e102fc46fd3d VishKapare10/dbda v1 v1 zip https://github.com \
    && unzip v1.zip \
    && ansible-playbook VishKapare10-dbda-b3325cb8d814b678db93ea472e57243425dc260c/code/debug_msg.yml

# Code file to execute when the docker container starts up (`entrypoint.sh`)
ENTRYPOINT ["/entrypoint.sh"]
RUN python3 random_no_gen.py
