import os
import sys
from flask import Flask, request
from flask_orator import Orator, jsonify
from orator.orm import belongs_to, has_many, belongs_to_many

# Configuration
DEBUG = True
ORATOR_DATABASES = {
    'default': 'roys',
    'roys': {
        'driver': 'sqlite',
        'database': os.path.join(os.path.dirname(__file__), 'roys.db')
    }
}

# Creating Flask application
app = Flask(__name__)
app.config.from_object(__name__)

# Initializing Orator
db = Orator(app)

class User(db.Model):
  __fillable__ = ['accessToken', 'email']
  __table__ = 'users'

  @belongs_to_many('subscribes','subscriber_id','subscribed_id')
  def blogs(self):
    return Blog

  def isSubscribing(self, blog):
            return self.blogs().where('subscribed_id', blog.id).exists()

  def subscribes(self,blog):
    if not self.isSubscribing(blog):
      self.blogs().attach(blog)

  def findRecommend(self):
    blogs = self.subscribes()

class Blog(db.Model):
  __fillable__ = ['url']
  __table__ = 'blogs'

  @belongs_to_many(
       'subscribes',
       'subscribed_id', 'subscriber_id'
  )
  def subscribers(self):
        return User

  @has_many
  def pages(self):
    return Page

class Subscribe(db.Model):
  __fillable__=['subscribed_id','subscriber_id']
  __table__ = 'subscribes'

class Page(db.Model):
  __fillable__ = ['pageUrl']
  @belongs_to
  def blogs(self):
    return Blog 

@app.route('/users', methods=['POST'])
def create_user():
  params=  request.get_json()
  _accessToken = params['accessToken']
  _email = params['email']
  user = User.first_or_create(accessToken=_accessToken,email=_email)
  blogs = params['blogs']

  for burl in blogs:
    blog = Blog.first_or_create(url=burl)
    user.subscribes(blog)
  
  return jsonify(user)

@app.route('/users/<int:user_id>/recommend', methods=['GET'])
def get_user_recommend(user_id):
  user = User.find_or_fail(user_id)
  #TODO(seralee)
  #return jsonify(user.findRecommend())
  return None 

if __name__ == '__main__':
  app.run()
