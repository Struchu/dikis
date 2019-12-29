FROM node:12-alpine

RUN apk --update add openjdk8-jre
RUN npm install -g firebase-tools

RUN npm install -g shadow-cljs
USER 1000:1000
WORKDIR /app

EXPOSE 8000 3333 9630

CMD ["npm", "run", "dev"]
