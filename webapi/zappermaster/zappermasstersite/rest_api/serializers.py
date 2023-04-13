from django.contrib.auth.models import User, Group
from rest_framework import serializers
from .models import Question, Choice, Remote, Button, Type, Manufacture


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
        fields = ['id', 'question_text', 'pub_date']


class ChoiceSerializer(serializers.HyperlinkedModelSerializer):
    question = serializers.PrimaryKeyRelatedField(queryset=Question.objects.all())

    class Meta:
        model = Choice
        fields = ['choice_text', 'question']


class ButtonSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Button
        fields = ['background_color', 'size', 'top_position_percent', 'left_position_percent', 'display_name', 'code']


class RemoteSerializer(serializers.HyperlinkedModelSerializer):
    buttons = ButtonSerializer(many=True)
    created = serializers.PrimaryKeyRelatedField(queryset=User.objects.all())
    type = serializers.PrimaryKeyRelatedField(queryset=Type.objects.all())
    manufacture = serializers.PrimaryKeyRelatedField(queryset=Manufacture.objects.all())
    download = serializers.IntegerField(
        source='downloads.count',
        read_only=True
    )

    class Meta:
        model = Remote
        fields = ['id', 'model_number', 'shared', 'buttons', 'created', 'type', 'manufacture', 'download']

    def create(self, validated_data):
        buttons_data = validated_data.pop('buttons')
        remote = Remote.objects.create(**validated_data)
        if buttons_data:
            for button_data in buttons_data:
                Button.objects.create(remote=remote, **button_data)
        return remote


class TypeSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Type
        fields = ['id', 'name', 'description']


class ManufactureSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Manufacture
        fields = ['id', 'name', 'description']


