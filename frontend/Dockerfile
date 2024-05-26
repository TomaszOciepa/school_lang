FROM node:alpine
# FROM --platform=linux/amd64 node:18-bullseye-slim
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN npm cache clear --force
RUN npm install
RUN npm install -g @angular/cli
RUN npm install
CMD ["ng", "serve", "--host", "0.0.0.0"]

    