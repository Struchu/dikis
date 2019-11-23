FROM node:12-alpine

WORKDIR /app
RUN apk --update add openjdk8-jre

EXPOSE 8000 3333 9630

USER 1000:1000

CMD ["npm", "run", "dev"]
