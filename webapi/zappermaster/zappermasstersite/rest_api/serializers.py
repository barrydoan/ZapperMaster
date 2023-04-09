from django.contrib.auth.models import User, Group
from rest_framework import serializers
from .models import Question, Remote, Button


class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ['url', 'username', 'email', 'groups']


class GroupSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Group
        fields = ['url', 'name']


class QuestionSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Question
        fields = ['question_text', 'pub_date']


class ButtonSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Button
        fields = ['background_color', 'size', 'top_position_percent', 'left_position_percent', 'display_name', 'code']


class RemoteSerializer(serializers.HyperlinkedModelSerializer):
    buttons = ButtonSerializer(many=True, read_only=True)

    class Meta:
        model = Remote
        fields = ['model_number', 'shared', 'buttons']
