import datetime
from django.db import models
from django.utils import timezone
from django.contrib.auth.models import User


class Question(models.Model):
    question_text = models.CharField(max_length=200)
    pub_date = models.DateTimeField("date published")

    def __str__(self):
        return self.question_text

    def was_published_recently(self):
        return self.pub_date >= timezone.now() - datetime.timedelta(days=1)


class Choice(models.Model):
    question = models.ForeignKey(Question, on_delete=models.CASCADE)
    choice_text = models.CharField(max_length=200)
    votes = models.IntegerField(default=0)

    def __str__(self):
        return self.choice_text


class Manufacture(models.Model):
    name = models.CharField(max_length=20)
    description = models.CharField(max_length=20)


class Type(models.Model):
    name = models.CharField(max_length=20)
    description = models.CharField(max_length=20)


class Remote(models.Model):
    downloads = models.ManyToManyField(User, related_name='downloads')
    created = models.ForeignKey(User, related_name='created', on_delete=models.CASCADE)
    type = models.ForeignKey(Type, related_name='type', on_delete=models.CASCADE)
    manufacture = models.ForeignKey(Manufacture, related_name='manufacture', on_delete=models.CASCADE)
    model_number = models.CharField(max_length=20)
    shared = models.BooleanField(default=False)

    def __str__(self):
        return self.model_number


class Button(models.Model):
    remote = models.ForeignKey(Remote,  related_name='buttons', on_delete=models.CASCADE)
    background_color = models.CharField(max_length=20)
    size = models.CharField(max_length=20)
    top_position_percent = models.FloatField(default=0.0)
    left_position_percent = models.FloatField(default=0.0)
    display_name = models.CharField(max_length=20)
    code = models.CharField(max_length=20)

    def __str__(self):
        return self.background_color
